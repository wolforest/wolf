package com.wolf.httpclient.vendor.okhttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.httpclient.HttpRequestBuilder;
import com.wolf.httpclient.Response;

import java.util.Objects;
import java.util.Set;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2021/1/18 10:33 下午
 **/
@Slf4j
public class OkHttpExecutor {
    private final OkHttpPool pool;
    private final Request request;
    HttpRequestBuilder requestBuilder;


    public static Response call(final OkHttpPool pool, HttpRequestBuilder requestBuilder) {
        OkHttpExecutor executor = new OkHttpExecutor(pool, requestBuilder);
        return executor.execute();
    }

    public OkHttpExecutor(final OkHttpPool pool, HttpRequestBuilder requestBuilder) {
        this.pool = pool;
        this.requestBuilder = requestBuilder;
        this.request = OkHttpBuilder.getInstance().build(requestBuilder);
    }

    public Response execute() {
        OkHttpClient client = pool.get(requestBuilder);

        try {
            okhttp3.Response response = client.newCall(request).execute();
            return formatResponse(response);
        } catch (Exception e) {
            // todolist io exception catch
            // request.url not include http port
            log.error("httpClient request fail: url:{}; error: {}", request.url(), e);
            return Response.error();
        }
    }

    public Response formatResponse(okhttp3.Response r) {
        if (null == r) {
            return Response.error();
        }

        try {
            String body = Objects.requireNonNull(r.body()).string();
            Response response = formatResponse(r, body);

            if (!response.isSuccess()) {
                log.info("HttpClient call request url: {}; request header: {}; request data: {}; response: {}; response header: {}; response data: {}",
                        request.url(),
                        requestBuilder.getHeader(),
                        requestBuilder.getRawBody(),
                        r,
                        response.getHeaders(),
                        body
                );
            }
            return response;
        } catch (Exception e) {
            log.error("HttpClient get body fail: request url: {}; request header: {}; request data: {}; response: {};",
                    request.url(),
                    requestBuilder.getHeader(),
                    requestBuilder.getRawBody(),
                    r,
                    e
            );
            return Response.error();
        }
    }


    public Response formatResponse(okhttp3.Response r, String body) {
        Response response = Response.builder()
                .successFlag(r.isSuccessful())
                .code(r.code())
                .body(body)
                .build();

        Headers headers = r.headers();
        Set<String> headerKeys = headers.names();
        if (CollectionUtil.isEmpty(headerKeys)) {
            return response;
        }

        for (String key : headerKeys) {
            String value = headers.get(key);
            response.addHeader(key, value);
        }

        return response;
    }
}
