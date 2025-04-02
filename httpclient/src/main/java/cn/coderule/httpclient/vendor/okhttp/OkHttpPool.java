package cn.coderule.httpclient.vendor.okhttp;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionPool;
import okhttp3.Dispatcher;
import okhttp3.OkHttpClient;
import okhttp3.tls.Certificates;
import okhttp3.tls.HandshakeCertificates;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.httpclient.HttpRequestBuilder;
import cn.coderule.httpclient.util.HttpLoggingInterceptor;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2021/1/18 10:39 下午
 **/
@Slf4j
public class OkHttpPool {

    private static final ConnectionPool POOL;
    private static final OkHttpClient CLIENT;
    private static final Dispatcher dispatcher;
    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

    static {
        int poolSize = Integer.parseInt(System.getProperty("httpclient.pool.size", "200"));
        int keepAlive = Integer.parseInt(System.getProperty("httpclient.keepAlive", "3600"));
        int threadCount = Integer.parseInt(System.getProperty("httpclient.thread.count", "8"));
        String executorType = System.getProperty("httpclient.executor.type", "default");
        if ("fix".equals(executorType)) {
            ExecutorService executor = new ThreadPoolExecutor(threadCount, threadCount, 0, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
            dispatcher = new Dispatcher(executor);
        } else {
            dispatcher = new Dispatcher();
        }
        POOL = new ConnectionPool(poolSize, keepAlive, TimeUnit.SECONDS);


        //以下参数对同步请求无效
//        int maxRequests = Integer.parseInt(System.getProperty("okhttp.requests.max", "500"));
//        int maxRequestsPerHost = Integer.parseInt(System.getProperty("okhttp.requests.host", "200"));
//        dispatcher.setMaxRequests(maxRequests);
//        dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);

        CLIENT = new OkHttpClient.Builder()
                .connectionPool(POOL)
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .dispatcher(dispatcher)
                .build();

    }

    public OkHttpPool() {

    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    public OkHttpClient get() {
        return CLIENT;
    }

    public OkHttpClient get(HttpRequestBuilder requestBuilder) {
        if (StringUtil.notBlank(requestBuilder.getKeyStore())) {
            return get(requestBuilder.getKeyStore(), requestBuilder.getKeyPassword());
        }

        if (CollectionUtil.notEmpty(requestBuilder.getCertificateList())) {
            return get(requestBuilder.getCertificateList());
        }

        return get();
    }

    public void release(OkHttpClient client) {

    }

    private OkHttpClient get(@NonNull String storePath, String password) {
        log.info("get okhttp client  by storePath: {},password: {}", storePath, password);
        KeyStore keyStore = getKeyStore(storePath, password);
        SSLContext sslContext;
        TrustManagerFactory trustManagerFactory;
        X509TrustManager trustManager;

        try {
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            trustManager = chooseTrustManager(trustManagers);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, password.toCharArray());

            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[]{trustManager}, null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException("create sslSocketFactory failed");
        }

        return CLIENT.newBuilder()
                .sslSocketFactory(sslContext.getSocketFactory(), trustManager)
                .addInterceptor(loggingInterceptor)
                .build();
    }

    private KeyStore getKeyStore(@NonNull String keyStore, String password) {
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

    private OkHttpClient get(@NonNull List<String> certificateList) {
        if (CollectionUtil.isEmpty(certificateList)) {
            throw new IllegalArgumentException("certificate list can't be empty");
        }

        HandshakeCertificates.Builder builder = new HandshakeCertificates.Builder();
        for (String cert : certificateList) {
            log.info("cert length:{}; data: {}", cert.length(), cert);
            X509Certificate certificate = Certificates.decodeCertificatePem(cert);
            builder.addTrustedCertificate(certificate);
        }

        HandshakeCertificates handshake = builder.build();
        return CLIENT.newBuilder()
                .sslSocketFactory(handshake.sslSocketFactory(), handshake.trustManager())
                .build();
    }


}
