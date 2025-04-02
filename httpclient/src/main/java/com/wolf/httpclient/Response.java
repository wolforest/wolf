package com.wolf.httpclient;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wolf.common.ds.map.ObjectMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.wolf.common.util.collection.MapUtil;
import com.wolf.common.util.lang.StringUtil;
import com.wolf.httpclient.cookie.Cookie;
import com.wolf.httpclient.exception.HttpRequestFailException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2020/11/18 1:30 下午
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response implements Serializable {
    private boolean successFlag;
    private int code;
    private String errorMessage;

    private Map<String, String> headers;
    private List<Cookie> cookies;
    private String body;

    private byte[] byteBody;

    public boolean isSuccess() {
        return successFlag;
    }

    public static Response error() {
        return Response.builder()
                .successFlag(false)
                .build();
    }

    public static Response error(int code) {
        return Response.builder()
                .successFlag(false)
                .code(code)
                .build();
    }

    public static Response error(int code, String message) {
        return Response.builder()
                .successFlag(false)
                .code(code)
                .errorMessage(message)
                .body(message)
                .build();
    }

    public String getString() {
        checkError();
        return getBody();
    }

    public String getStringWithoutCheck() {
        checkError();
        return getBody();
    }

    public String getBody() {
        if (StringUtil.notBlank(body)) {
            return body;
        }

        return "";
    }

    public JSONObject getJson() {
        checkError();
        return getJsonWithoutCheck();
    }

    public JSONObject getJsonWithoutCheck() {
        if (StringUtil.isBlank(body)) {
            return new JSONObject();
        }

        return JSON.parseObject(body);
    }

    public ObjectMap getObjectMap() {
        return new ObjectMap(getJson());
    }

    public <T> T getObject(Class<T> type) {
        checkError();

        if (StringUtil.isBlank(body)) {
            return null;
        }

        return JSONObject.parseObject(body, type);
    }

    public String getHeadersString() {
        if (MapUtil.isEmpty(headers)) {
            return "{}";
        }

        return JSONObject.toJSONString(headers);
    }

    public String getHeader(String key) {
        checkError();

        if (StringUtil.isBlank(key)) {
            return null;
        }

        return headers.get(key);
    }

    public void addHeader(String key, String value) {
        if (StringUtil.isBlank(key)) {
            return;
        }

        initHeaders();
        headers.put(key, value);
    }

    private void initHeaders() {
        if (headers != null) {
            return;
        }

        headers = new HashMap<>();
    }

    private void checkError() {
        checkError("unknown error");
    }

    private void checkError(String msg) {
        if (body == null) {
            body = "";
        }

        if (successFlag) {
            return;
        }

        throw new HttpRequestFailException(msg);
    }
}
