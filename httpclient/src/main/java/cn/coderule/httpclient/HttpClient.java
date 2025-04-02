package cn.coderule.httpclient;

import cn.coderule.httpclient.vendor.netty.ReactorNettyHttpClientExecutor;
import cn.coderule.httpclient.vendor.okhttp.OkHttpExecutor;
import cn.coderule.httpclient.vendor.okhttp.OkHttpPool;
import lombok.extern.slf4j.Slf4j;
import cn.coderule.common.lang.exception.lang.IllegalArgumentException;
import cn.coderule.httpclient.cookie.CookieManager;
import cn.coderule.httpclient.cookie.CookiePolicyEnum;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Facade of HttpClient
 *
 * @author Wingle
 * @since 2020/11/18 1:09 下午
 **/
@Slf4j
public class HttpClient {
    private static final OkHttpPool POOL = new OkHttpPool();
    private static CookieManager cookieManager;
    private static boolean enableCookieManagement;
    /**
     * vendor list:
     * netty (default)
     * okhttp
     * unirest
     */
    private static final String vendor = System.getProperties().getProperty("httpclient.vendor", "netty");

    public static void init(HttpClientConfig config) {
        cookieManager = new CookieManager(config);
        ReactorNettyHttpClientExecutor.init(config);
        ReactorNettyHttpClientExecutor.setCookieManager(cookieManager);

        enableCookieManagement = config.isEnableCookieManagement();
    }

    public static HttpRequestBuilder builder() {
        return new HttpRequestBuilder();
    }

    /**
     * blocking method
     **/
    public static Response execute(HttpRequestBuilder requestBuilder) {
        if (enableCookieManagement) {
            cookieManager.loadCookies(requestBuilder);
        }
        Response response = null;

        switch (vendor) {
            case "jdk":
                log.warn("jdk native httpclient is not supported");
                //return JDK17HttpClientExecutor.call(POOL, requestBuilder);
                break;
            case "netty":
                response = ReactorNettyHttpClientExecutor.call(POOL, requestBuilder);
                break;
            default:
                response = OkHttpExecutor.call(POOL, requestBuilder);
        }

        if (response == null) {
            return null;
        }

        if (enableCookieManagement) {
            cookieManager.saveCookies(requestBuilder, response);
        }

        return response;
    }

//    public static Response executeUseOkHttp(HttpRequestBuilder requestBuilder) {
//        return OkHttpExecutor.call(POOL, requestBuilder);
//    }

    public static Response get(String url) {
        return HttpClient.builder()
                .get(url)
                .execute();
    }

    public static Response post(String url, Object data) {
        return HttpClient.builder()
                .url(url)
                .post(data)
                .execute();
    }

    public static Response form(String url, Map<String, Object> data) {
        return HttpClient.builder()
                .url(url)
                .form(data)
                .execute();
    }

    public static void setCookiePolicy(CookiePolicyEnum policy) {
        if (null == cookieManager) {
            throw new IllegalArgumentException("CookieManager has not been initiated");
        }

        cookieManager.setPolicy(policy);
    }

    public static void setCookieJar(String jarName) {
        if (null == cookieManager) {
            throw new IllegalArgumentException("CookieManager has not been initiated");
        }

        cookieManager.setJarName(jarName);
    }

    public static void clearCookie() {
        if (null == cookieManager) {
            throw new IllegalArgumentException("CookieManager has not been initiated");
        }

        cookieManager.clearCookies();
    }

    /**
     * non-blocking method
     **/
    public static CompletableFuture<Response> asyncExecute(HttpRequestBuilder requestBuilder) {
        return null;
    }

    public static Map<String, Response> groupExecute(Collection<HttpRequestBuilder> requestBuilders) {
        return null;
    }
}
