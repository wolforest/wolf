package com.wolf.httpclient;

import com.wolf.httpclient.enums.ContentTypeEnum;
import com.wolf.httpclient.enums.HttpMethodEnum;
import lombok.Data;
import lombok.NonNull;
import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.common.util.collection.MapUtil;
import com.wolf.common.util.lang.StringUtil;
import com.wolf.common.util.net.URLUtil;
import com.wolf.httpclient.cookie.Cookie;
import com.wolf.httpclient.cookie.CookieBuilder;
import com.wolf.httpclient.cookie.CookiePolicyEnum;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2020/11/18 1:10 下午
 **/
@Data
public class HttpRequestBuilder {
    private String requestId;

    private String url;
    private HttpMethodEnum method;
    private ContentTypeEnum contentType;

    private CookiePolicyEnum cookiePolicy;
    private String cookieJar;
    private List<Cookie> cookies;

    private Map<String, String> header;
    private Map<String, Object> query;

    private String stringBody;
    private Object objectBody;
    private Map<String, Object> mapBody;

    @Deprecated
    private Map<String, Object> formBody;

    private Duration timeout;
    private Duration connectTimeout;
    private Duration readTimeout;
    private Duration writeTimeout;

    private String keyStore;
    private String keyPassword;
    private List<String> certificateList;

    public HttpRequestBuilder(@NonNull String requestId) {
        this.requestId = requestId;
        method = HttpMethodEnum.GET;
        header = new LinkedHashMap<>();
        query = new LinkedHashMap<>();
        cookies = new ArrayList<>();
        certificateList = new ArrayList<>();
    }

    public HttpRequestBuilder() {
        this(StringUtil.uuid());
    }

    /**
     * execute methods start
     **/
    public Response execute() {
        return HttpClient.execute(this);
    }

//    public Response executeUseOkhttp() {
//        return HttpClient.executeUseOkHttp(this);
//    }

    public CompletableFuture<Response> asyncExecute() {
        return HttpClient.asyncExecute(this);
    }


    /**
     * builder methods start
     **/
    public HttpRequestBuilder certificate(@NonNull String keyStore, @NonNull String keyPassword) {
        this.keyStore = keyStore;
        this.keyPassword = keyPassword;

        return this;
    }

    public HttpRequestBuilder certificate(String cert) {
        certificateList.add(cert);

        return this;
    }

    public HttpRequestBuilder timeout(Duration timeout) {
        if (timeout == null || timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("invalid timeout for HttpClient: " + timeout);
        }

        this.timeout = timeout;
        return this;
    }

    public HttpRequestBuilder connectTimeout(Duration timeout) {
        if (timeout == null || timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("invalid connectTimeout for HttpClient: " + timeout);
        }

        this.connectTimeout = timeout;
        return this;
    }

    public HttpRequestBuilder readTimeout(Duration timeout) {
        if (timeout == null || timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("invalid readTimeout for HttpClient: " + timeout);
        }

        this.readTimeout = timeout;
        return this;
    }

    public HttpRequestBuilder writeTimeout(Duration timeout) {
        if (timeout == null || timeout.isNegative() || timeout.isZero()) {
            throw new IllegalArgumentException("invalid writeTimeout for HttpClient: " + timeout);
        }

        this.writeTimeout = timeout;
        return this;
    }

    public HttpRequestBuilder url(String url) {
        if (StringUtil.isBlank(url)) {
            throw new IllegalArgumentException("url for HttpClient can't be blank");
        }

        this.url = url;
        return this;
    }

    public HttpRequestBuilder contentType(@NonNull ContentTypeEnum contentType) {
        this.contentType = contentType;

        return this;
    }

    public HttpRequestBuilder bearer(String token) {
        return header("Authorization", StringUtil.join("Bearer ", token));
    }

    public HttpRequestBuilder auth(String token) {
        return header("Authorization", token);
    }

    public HttpRequestBuilder auth(String token, String type) {
        return header("Authorization", StringUtil.join(type, " ", token));
    }

    public HttpRequestBuilder basicAuth(String username, String password) {
        if (StringUtil.isBlank(username) && StringUtil.isBlank(password)) {
            return this;
        }

        String token = StringUtil.join(username, ":", password);
        token = Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));

        return header("Authorization", StringUtil.join("Basic ", token));
    }

    public HttpRequestBuilder header(String key, String value) {
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http header key can't be blank");
        }

        if (value == null) {
            throw new IllegalArgumentException("Http header value can't be null");
        }

        header.put(key, value);
        return this;
    }

    public HttpRequestBuilder header(Map<String, String> map) {
        if (MapUtil.isEmpty(map)) {
            return this;
        }

        header.putAll(map);
        return this;
    }

    public CookieBuilder cookieBuilder() {
        return new CookieBuilder(this);
    }

    public HttpRequestBuilder addCookies(List<Cookie> cookieList) {
        if (CollectionUtil.isEmpty(cookieList)) {
            return this;
        }

        cookies.addAll(cookieList);
        return this;
    }

    public HttpRequestBuilder addCookie(@NonNull Cookie cookie) {
        cookies.add(cookie);

        return this;
    }

    public HttpRequestBuilder cookiePolicy(CookiePolicyEnum policyEnum) {
        this.cookiePolicy = policyEnum;
        return this;
    }

    public HttpRequestBuilder cookie(String key, String value) {
        return cookie(key, value, null);
    }

    public HttpRequestBuilder cookie(String key, String value, Long maxAge) {
        return cookie(key, value, maxAge, null);
    }

    public HttpRequestBuilder cookie(String key, String value, Long maxAge, String domain) {
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http header key can't be blank");
        }

        if (value == null) {
            throw new IllegalArgumentException("Http header value can't be null");
        }

        Cookie cookie = CookieBuilder.newInstance().nameValue(key, value).maxAge(maxAge).domain(domain).build();
        return addCookie(cookie);
    }

    public HttpRequestBuilder cookie(Map<String, String> map) {
        if (MapUtil.isEmpty(map)) {
            return this;
        }

        for (Map.Entry<String, String> entry : map.entrySet()) {
            cookie(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public HttpRequestBuilder query(String key, Object value) {
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http query key can't be blank");
        }

        if (value == null) {
            throw new IllegalArgumentException("Http query value can't be null");
        }

        query.put(key, value);
        return this;
    }

    public HttpRequestBuilder query(Map<String, Object> map) {
        if (MapUtil.isEmpty(map)) {
            return this;
        }

        query.putAll(map);
        return this;
    }

    public HttpRequestBuilder form(String key, Object value) {
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http form key can't be blank");
        }

        method = HttpMethodEnum.POST;
        initFormBody();
        if (value == null) {
            value = "";
        }

        formBody.put(key, value);
        return this;
    }

    public HttpRequestBuilder form(Map<String, Object> map) {
        method = HttpMethodEnum.POST;
        contentType = ContentTypeEnum.FORM;
        if (MapUtil.isEmpty(map)) {
            return this;
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            form(entry.getKey(), entry.getValue());
        }

        return this;
    }

    public HttpRequestBuilder post(String body) {
        return post(body, ContentTypeEnum.TEXT);
    }

    public HttpRequestBuilder post(Object body) {
        return post(body, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder post(Map<String, Object> data) {
        return post(data, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder post(Map<String, Object> data, ContentTypeEnum contentType) {
        method = HttpMethodEnum.POST;
        if (MapUtil.isEmpty(data)) {
            return this;
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            post(entry.getKey(), entry.getValue(), contentType);
        }

        return this;
    }

    public HttpRequestBuilder post(@NonNull String key, Object value) {
        return post(key, value, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder post(@NonNull String key, Object value, @NonNull ContentTypeEnum contentType) {
        method = HttpMethodEnum.POST;
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http post map key can't be blank");
        }

        initMapBody(contentType);
        if (value == null) {
            value = "";
        }

        mapBody.put(key, value);
        return this;
    }

    public HttpRequestBuilder post(String body, ContentTypeEnum contentType) {
        stringBody = body;
        method = HttpMethodEnum.POST;

        return contentType(contentType);
    }

    public HttpRequestBuilder post(Object body, ContentTypeEnum contentType) {
        if (body instanceof String) {
            stringBody = (String) body;
        } else {
            objectBody = body;
        }

        method = HttpMethodEnum.POST;
        return contentType(contentType);
    }

    public HttpRequestBuilder put(String body) {
        return put(body, ContentTypeEnum.TEXT);
    }

    public HttpRequestBuilder put(Object body) {
        return put(body, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder put(String body, ContentTypeEnum contentType) {
        stringBody = body;
        method = HttpMethodEnum.PUT;
        return contentType(contentType);
    }

    public HttpRequestBuilder put(Object body, ContentTypeEnum contentType) {
        objectBody = body;
        method = HttpMethodEnum.PUT;
        return contentType(contentType);
    }

    public HttpRequestBuilder put(Map<String, Object> data) {
        return put(data, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder put(Map<String, Object> data, ContentTypeEnum contentType) {
        method = HttpMethodEnum.PUT;
        if (MapUtil.isEmpty(data)) {
            return this;
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            put(entry.getKey(), entry.getValue(), contentType);
        }

        return this;
    }

    public HttpRequestBuilder put(@NonNull String key, Object value) {
        return put(key, value, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder put(@NonNull String key, Object value, @NonNull ContentTypeEnum contentType) {
        method = HttpMethodEnum.PUT;
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http post map key can't be blank");
        }

        initMapBody(contentType);
        if (value == null) {
            value = "";
        }

        mapBody.put(key, value);
        return this;
    }

    public HttpRequestBuilder delete(String body) {
        return delete(body, ContentTypeEnum.TEXT);
    }

    public HttpRequestBuilder delete(Object body) {
        return delete(body, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder delete(String body, ContentTypeEnum contentType) {
        stringBody = body;
        method = HttpMethodEnum.DELETE;
        return contentType(contentType);
    }

    public HttpRequestBuilder delete(Object body, ContentTypeEnum contentType) {
        objectBody = body;
        method = HttpMethodEnum.DELETE;
        return contentType(contentType);
    }

    public HttpRequestBuilder delete(Map<String, Object> data) {
        return delete(data, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder delete(Map<String, Object> data, ContentTypeEnum contentType) {
        method = HttpMethodEnum.DELETE;
        if (MapUtil.isEmpty(data)) {
            return this;
        }

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            delete(entry.getKey(), entry.getValue(), contentType);
        }

        return this;
    }

    public HttpRequestBuilder delete(@NonNull String key, Object value) {
        return delete(key, value, ContentTypeEnum.JSON);
    }

    public HttpRequestBuilder delete(@NonNull String key, Object value, @NonNull ContentTypeEnum contentType) {
        method = HttpMethodEnum.DELETE;
        if (StringUtil.isBlank(key)) {
            throw new IllegalArgumentException("Http post map key can't be blank");
        }

        initMapBody(contentType);
        if (value == null) {
            value = "";
        }

        mapBody.put(key, value);
        return this;
    }

    public HttpRequestBuilder get(String url) {
        return get(url, null);
    }

    public HttpRequestBuilder get(String url, Map<String, Object> params) {
        if (StringUtil.isBlank(url)) {
            throw new IllegalArgumentException("url for HttpClient.get can't be blank");
        }

        if (MapUtil.notEmpty(params)) {
            query.putAll(params);
        }

        this.url = url;
        return this;
    }

    /**
     * private methods start
     **/
    private void initFormBody() {
        contentType = ContentTypeEnum.FORM;
        if (formBody != null) {
            return;
        }

        formBody = new LinkedHashMap<>();
    }

    private void initMapBody(ContentTypeEnum type) {
        contentType = type;
        if (mapBody != null) {
            return;
        }

        mapBody = new LinkedHashMap<>();
    }

    public String getUrl() {
        if (MapUtil.isEmpty(query)) {
            return url;
        }

        String joiner = -1 == url.indexOf('?') ? "?" : "&";
        url = StringUtil.join(url, joiner, URLUtil.encodeQuery(query));
        query.clear();

        return url;
    }

    public Object getRawBody() {
        return null != formBody ? formBody : (null != mapBody ? mapBody : (null != stringBody ? stringBody : (null != objectBody ? objectBody : null)));
    }

}
