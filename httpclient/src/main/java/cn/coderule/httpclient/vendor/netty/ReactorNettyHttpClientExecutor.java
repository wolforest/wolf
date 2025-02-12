package cn.coderule.httpclient.vendor.netty;

import cn.coderule.httpclient.vendor.okhttp.OkHttpPool;
import cn.coderule.httpclient.enums.ContentTypeEnum;
import cn.coderule.httpclient.enums.HttpMethodEnum;
import io.netty.channel.ChannelOption;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.handler.codec.http.HttpMethod;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.Http11SslContextSpec;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.HttpClientRequest;
import reactor.netty.http.client.HttpClientResponse;
import reactor.netty.resources.ConnectionProvider;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.string.JSONUtil;
import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.httpclient.HttpClientConfig;
import cn.coderule.httpclient.HttpRequestBuilder;
import cn.coderule.httpclient.Response;
import cn.coderule.httpclient.cookie.Cookie;
import cn.coderule.httpclient.cookie.CookieManager;
import cn.coderule.httpclient.util.HttpLoggingInterceptor;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.ConnectException;
import java.security.KeyStore;
import java.time.Duration;
import java.util.List;
import java.util.Map;

/**
 * ReactorNettyHttp
 *
 * @author: YIK
 * @since: 2022/6/11 9:39 PM
 */
@Slf4j
public class ReactorNettyHttpClientExecutor {
    static reactor.netty.http.client.HttpClient commonClient;
    static reactor.netty.http.client.HttpClient secureClient;
    static HttpClientConfig clientConfig;
    static CookieManager cookieManager;

    public static void setCookieManager(CookieManager manager) {
        cookieManager = manager;
    }

    public static void init(HttpClientConfig config) {
        clientConfig = config;
        int poolSize = Integer.parseInt(System.getProperty("httpclient.pool.size", "800"));
        int keepAlive = Integer.parseInt(System.getProperty("httpclient.keepAlive", "3600"));

        ConnectionProvider provider = ConnectionProvider.builder("custom")
            .maxConnections(poolSize)
            .maxIdleTime(Duration.ofSeconds(keepAlive - 120L))
            .maxLifeTime(Duration.ofSeconds(keepAlive))
            .pendingAcquireTimeout(Duration.ofSeconds(60L))
            .evictInBackground(Duration.ofSeconds(120L)).build();

        commonClient = reactor.netty.http.client.HttpClient.create(provider)
            .responseTimeout(Duration.ofSeconds(10))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
            .option(EpollChannelOption.TCP_KEEPIDLE, 300)
            .option(EpollChannelOption.TCP_KEEPINTVL, 60)
            .option(EpollChannelOption.TCP_KEEPCNT, 8);

        commonClient.keepAlive(true);
        commonClient.warmup().block();

        if (isKeyStoreConfig()) {
            TrustManager trustManager = SSLConfiguration.getTrustManager(clientConfig.getKeyStore(), clientConfig.getKeyStorePassword());
            KeyManagerFactory keyManagerFactory = SSLConfiguration.getKeyManagerFactory(clientConfig.getKeyStore(), clientConfig.getKeyStorePassword());
            Http11SslContextSpec yesBankHttp11SslContextSpec = Http11SslContextSpec.forClient()
                .configure(builder -> builder
                    .trustManager(trustManager)
                    .keyManager(keyManagerFactory)
                );
            secureClient = commonClient.secure(spec -> spec.sslContext(yesBankHttp11SslContextSpec)
                    .handshakeTimeout(Duration.ofSeconds(30))
                    .closeNotifyFlushTimeout(Duration.ofSeconds(10))
                    .closeNotifyReadTimeout(Duration.ofSeconds(10)))
                .responseTimeout(Duration.ofSeconds(30));
        }

    }

    public static Response call(final OkHttpPool pool, HttpRequestBuilder requestBuilder) {
        reactor.netty.http.client.HttpClient client = chooseClient(requestBuilder);

        if (null != requestBuilder.getTimeout()) {
            //Maybe you should cache the client to the hashmap
            //This is because a new client will be created after the timeout is changed
            client =  client.responseTimeout(requestBuilder.getTimeout());
        }

        client = buildHeader(client, requestBuilder);
        HttpMethod method = chooseMethod(requestBuilder);
        final String body = buildBody(requestBuilder);
        HttpClient.ResponseReceiver<?> receiver = null;
        if (ContentTypeEnum.FORM == requestBuilder.getContentType()) {
            receiver = client.request(method).uri(requestBuilder.getUrl()).sendForm((httpRequest, form) -> {
                buildCookie(httpRequest, requestBuilder);
                if (requestBuilder.getFormBody() != null) {
                    requestBuilder.getFormBody().forEach((k, v) -> form.attr(k, v == null ? "" : v.toString()));
                } else {
                    log.warn("contentType is form,but formBody is null,ignore it");
                }

            });
        } else {
            receiver = client.request(method).uri(requestBuilder.getUrl()).send((httpRequest, out) -> {
                buildCookie(httpRequest, requestBuilder);
                return out.sendString(Mono.just(body)).then();
            });
        }

        long requestAtMillis = System.currentTimeMillis();
        Mono<Response> monoResponse = receiver.responseSingle((resp, bytes) -> {
                int statusCode = resp.status().code();
                boolean isSuccess = statusCode >= 200 && statusCode <= 299;
                List<Cookie> cookies = CookieConverter.to(resp.cookies());
                return isSuccess ? justSuccess(resp, bytes, statusCode, cookies) : justError(statusCode, bytes, cookies);
            })
            .onErrorResume(ConnectException.class, e -> justError(504, "ERR_CONNECTION_TIMED_OUT"))
            .onErrorResume(Throwable.class, e -> justError(504, e.getMessage()));

        Response response = monoResponse.block();

        if (response != null) {
            logForMetric(method, body, requestAtMillis, requestBuilder.getUrl(), response.getCode(), response.isSuccess());
        } else {
            log.warn("response is null, response timeout");
            logForMetric(method, body, requestAtMillis, requestBuilder.getUrl(), 504, false);
            return Response.builder().successFlag(false).code(504).build();
        }

        return response;
    }

    @NotNull
    private static Mono<Response> justError(int statusCode, String errorMessage) {
        return Mono.just(Response.builder()
            .successFlag(false)
            .code(statusCode)
            .body(errorMessage)
            .errorMessage(errorMessage)
            .build()
        );
    }

    @NotNull
    private static Mono<Response> justError(int statusCode, ByteBufMono bytes, List<Cookie> cookies) {
        return bytes.asString()
            .switchIfEmpty(Mono.just("{}"))
            .map(
                respBodyString -> Response
                    .builder()
                    .successFlag(false)
                    .code(statusCode)
                    .body(respBodyString)
                    .errorMessage(respBodyString)
                    .cookies(cookies)
                    .build()
            );
    }

    @NotNull
    private static Mono<Response> justSuccess(HttpClientResponse resp, ByteBufMono bytes, int statusCode, List<Cookie> cookies) {
        if (resp.responseHeaders().contains("Content-Type") && resp.responseHeaders().get("Content-Type").startsWith(ContentTypeEnum.PDF.getHeader())) {
            return bytes.asByteArray().map(respBody -> Response
                .builder()
                .successFlag(true)
                .code(statusCode)
                .byteBody(respBody)
                .cookies(cookies)
                .build());
        }
        return bytes.asString()
            .switchIfEmpty(Mono.just("{}"))
            .map(
                respBodyString -> Response
                    .builder()
                    .successFlag(true)
                    .code(statusCode)
                    .body(respBodyString)
                    .cookies(cookies)
                    .build()
            );
    }

    private Map<String, String> parseResponseHeader() {
        return null;
    }


    private static boolean isKeyStoreConfig() {
        return clientConfig != null && StringUtil.notBlank(clientConfig.getKeyStore());
    }

    private static String buildBody(HttpRequestBuilder requestBuilder) {
        ContentTypeEnum contentType = requestBuilder.getContentType();
        if (contentType == null) {
            contentType = ContentTypeEnum.HTML;
        }
        Map<String, Object> mapBody = requestBuilder.getMapBody();
        Object objectBody = requestBuilder.getObjectBody();
        String stringBody = requestBuilder.getStringBody();
        HttpMethodEnum method = requestBuilder.getMethod();
        String body = "";
        switch (contentType) {
            case JSON:
            case JOSE:
                if (null != mapBody) {
                    body = JSONUtil.toJSONString(mapBody);
                } else if (null != objectBody) {
                    if (objectBody instanceof String) {
                        body = (String) objectBody;
                    } else {
                        body = JSONUtil.toJSONString(objectBody);
                    }

                } else if (StringUtil.notBlank(stringBody)) {
                    body = stringBody;
                } else {
                    body = "{}";
                }
                break;
            case HTML:
            case TEXT:
            case XML:
                body = StringUtil.isBlank(stringBody) ? stringBody : "";
                break;
            case FORM:
                //TODO bad implement
                //body = buildFormBody(requestBuilder);
                break;
            case FILE:
            case PDF:
            case MSWORD:
                //TODO build file body
                break;
            default:
                throw new IllegalArgumentException("invalid contentType for HttpClient: " + contentType.getName());
        }
        if (HttpMethodEnum.GET == method) {
            body = "";
        }
        if (null == body) {
            body = "";
        }
        return body;
    }


    private static String buildFormBody(HttpRequestBuilder requestBuilder) {
        Map<String, Object> formBody = requestBuilder.getFormBody();
        StringBuilder sb = new StringBuilder();
        if (formBody != null) {
            formBody.forEach((k, v) -> {
                sb.append(k);
                sb.append("=");
                sb.append(v);
                sb.append("&");
            });
        }
        return sb.substring(0, sb.length() - 1);
    }

    private static void logForMetric(HttpMethod method, String body, long requestAtMillis, String url, int statusCode, boolean isSuccess) {
        long receivedAtMillis = System.currentTimeMillis();
        long rtInMillis = receivedAtMillis - requestAtMillis;
        HttpLoggingInterceptor.log(isSuccess, rtInMillis, method.name(), url, "HTTP", statusCode, body.getBytes().length);
    }

    private static HttpClient buildHeader(HttpClient client, HttpRequestBuilder requestBuilder) {
        if (requestBuilder.getContentType() == null) {
            requestBuilder.header("Content-Type", ContentTypeEnum.HTML.getHeader());
        } else if (!ContentTypeEnum.FORM.equals(requestBuilder.getContentType())) {
            requestBuilder.header("Content-Type", requestBuilder.getContentType().getHeader());
        }

        return client.headers(headers -> requestBuilder.getHeader().forEach((k, v) -> headers.add(k, v)));
    }

    private static void buildCookie(HttpClientRequest httpRequest, HttpRequestBuilder requestBuilder) {
        if (CollectionUtil.isEmpty(requestBuilder.getCookies())) {
            return;
        }

        for (Cookie cookie : requestBuilder.getCookies()) {
            httpRequest.addCookie(CookieConverter.from(cookie));
        }
    }

    private static HttpMethod chooseMethod(HttpRequestBuilder requestBuilder) {
        HttpMethod method;
        HttpMethodEnum originMethod = requestBuilder.getMethod();
        switch (originMethod) {
            case TRACE:
                method = HttpMethod.TRACE;
                break;
            case OPTIONS:
                method = HttpMethod.OPTIONS;
                break;
            case DELETE:
                method = HttpMethod.DELETE;
                break;
            case PATCH:
                method = HttpMethod.PATCH;
                break;
            case PUT:
                method = HttpMethod.PUT;
                break;
            case HEAD:
                method = HttpMethod.HEAD;
                break;
            case GET:
                method = HttpMethod.GET;
                break;
            default:
                method = HttpMethod.POST;
        }
        return method;
    }

    private static reactor.netty.http.client.HttpClient chooseClient(HttpRequestBuilder requestBuilder) {
        reactor.netty.http.client.HttpClient client = commonClient;

//        URL url = null;
//        try {
//            url = new URL(requestBuilder.getUrl());
//        } catch (MalformedURLException e) {
//            log.error(e.getMessage(), e);
//        }
//        final String host = url.getHost();
//        if (ArrayUtil.inArray(host , "xxxbank.in" )) {
//            if (secureClient != null) {
//                client = secureClient;
//            } else {
//                throw new RuntimeException("secure client is not initialized, but is called");
//            }
//        }

        return client;
    }


    static class SSLConfiguration {

        public static TrustManager getTrustManager(String path, String password) {
            KeyStore keyStore = getKeyStore(path, password);
            TrustManagerFactory trustManagerFactory;
            X509TrustManager trustManager;
            try {
                trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                trustManager = chooseTrustManager(trustManagers);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new IllegalArgumentException("create TrustManager failed");
            }
            return trustManager;
        }


        @SneakyThrows
        public static KeyManagerFactory getKeyManagerFactory(String path, String password) {
            KeyStore keyStore = getKeyStore(path, password);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password.toCharArray());
            return keyManagerFactory;
        }

        public static KeyStore getKeyStore(@NonNull String keyStore, String password) {
            try {
                KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
                File file = new File(keyStore);
                try (InputStream fis = new FileInputStream(file.getAbsoluteFile())) {
                    ks.load(fis, password.toCharArray());
                }

                return ks;
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new IllegalArgumentException("Invalid KeyStore or password");
            }
        }

        private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
            for (TrustManager trustManager : trustManagers) {
                if (trustManager instanceof X509TrustManager) {
                    return (X509TrustManager) trustManager;
                }
            }
            return null;
        }
    }
}
