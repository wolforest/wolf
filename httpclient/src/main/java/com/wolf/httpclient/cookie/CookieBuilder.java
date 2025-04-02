package com.wolf.httpclient.cookie;

import lombok.NonNull;
import com.wolf.httpclient.HttpRequestBuilder;

import java.time.LocalDateTime;

public class CookieBuilder {
    private final HttpRequestBuilder httpRequestBuilder;
    private Cookie cookie;

    public static CookieBuilder newInstance(HttpRequestBuilder httpRequestBuilder) {
        return new CookieBuilder(httpRequestBuilder);
    }

    public static CookieBuilder newInstance() {
        return new CookieBuilder();
    }

    public CookieBuilder() {
        this(null);
    }

    public CookieBuilder(HttpRequestBuilder httpRequestBuilder) {
        this.httpRequestBuilder = httpRequestBuilder;
        cookie = new Cookie();
    }

    public CookieBuilder key(@NonNull String key) {
        cookie.setName(key);
        return this;
    }

    public CookieBuilder value(@NonNull String value) {
        cookie.setValue(value);
        return this;
    }

    public CookieBuilder maxAge(@NonNull Long maxAge) {
        cookie.setMaxAge(maxAge);
        return this;
    }

    public CookieBuilder nameValue(@NonNull String key, @NonNull String value) {
        cookie.setName(key);
        cookie.setValue(value);

        return this;
    }

    public CookieBuilder domain(@NonNull String domain) {
        return this;
    }

    public CookieBuilder path(@NonNull String path) {
        cookie.setPath(path);
        return this;
    }

    public CookieBuilder expires(@NonNull LocalDateTime expires) {
        cookie.setExpires(expires);
        return this;
    }

    public CookieBuilder secure() {
        cookie.setSecure(true);
        return this;
    }

    public CookieBuilder httpOnly() {
        cookie.setHttpOnly(true);
        return this;
    }

    public CookieBuilder sameSite(@NonNull SameSiteEnum sameSiteEnum) {
        cookie.setSameSite(sameSiteEnum);
        return this;
    }


    public Cookie build() {
        return cookie;
    }

    public CookieBuilder cookie() {
        httpRequestBuilder.addCookie(cookie);
        return new CookieBuilder(httpRequestBuilder);
    }

    public HttpRequestBuilder buildCookie() {
        return httpRequestBuilder;
    }
}
