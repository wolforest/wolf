package com.wolf.httpclient.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;

import java.io.IOException;

/**
 * Okhttp 日志输出到logback
 *
 * @author: YIK
 * @since: 2022/1/28 10:50 AM
 */
@Slf4j
public class HttpLoggingInterceptor implements Interceptor {


    public static void log(boolean isSuccess, long rtInMillis, String method, String url, String protocol, int statusCode, long bodySend) {
        if (isSuccess) {
            log.info("{} {} \"{}\" {} {}", rtInMillis, method + " " + url + " " + protocol, statusCode, bodySend, System.currentTimeMillis());
        } else {
            log.error("{} {} \"{}\" {} {}", rtInMillis, method + " " + url + " " + protocol, statusCode, bodySend, System.currentTimeMillis());
        }
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        try {
            interceptLog(request, response);
        } catch (Exception e) {
            log.error("interceptLog occur error", e);
        }

        return response;
    }

    private void interceptLog(Request request, Response response) throws IOException {
        String method = request.method();
        long requestAtMillis = response.sentRequestAtMillis();
        long receivedAtMillis = response.receivedResponseAtMillis();
        double rtInMillis = (receivedAtMillis - requestAtMillis) / 1000.00;

        String url = request.url().url().toString();
        int statusCode = response.code();
        long bodySend = 0;
        if (request.body() != null) {
            bodySend = request.body().contentLength();
        }

        String protocol = response.protocol().toString();

        MDC.put("hostname", request.url().host());
        if (response.isSuccessful()) {
            log.info("{} {} \"{}\" {} {}", rtInMillis, method + " " + url + " " + protocol, statusCode, bodySend, System.currentTimeMillis());
        } else {
            log.error("{} {} \"{}\" {} {}", rtInMillis, method + " " + url + " " + protocol, statusCode, bodySend, System.currentTimeMillis());
        }
    }
}
