package com.wolf.httpclient.util;

import lombok.Getter;
import lombok.NonNull;
import com.wolf.common.lang.exception.lang.IllegalArgumentException;
import com.wolf.httpclient.HttpRequestBuilder;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestMap {
    /**
     * Map (
     * key: namespace
     * Map (
     * key: requestName
     * value: HttpRequestBuilder
     * )
     * )
     */
    @Getter
    private final Map<String, Map<String, HttpRequestBuilder>> map;
    private String currentNamespace;

    public HttpRequestMap() {
        map = new HashMap<>();
        currentNamespace = null;
    }

    public HttpRequestBuilder get(@NonNull String namespace, @NonNull String name) {
        Map<String, HttpRequestBuilder> requestMap = getByNamespace(namespace);
        if (requestMap == null) {
            return null;
        }

        return requestMap.get(name);
    }

    public HttpRequestMap namespace(@NonNull String namespace) {
        currentNamespace = namespace;
        return this;
    }

    public HttpRequestMap put(@NonNull String name, HttpRequestBuilder request) {
        if (currentNamespace == null) {
            throw new IllegalArgumentException("namespace can't be null");
        }

        Map<String, HttpRequestBuilder> requestMap = getByNamespace(currentNamespace);
        if (requestMap == null) {
            requestMap = new HashMap<>();
            map.put(currentNamespace, requestMap);
        }

        requestMap.put(name, request);
        return this;
    }

    public Map<String, HttpRequestBuilder> getByNamespace(@NonNull String namespace) {
        return map.get(namespace);
    }

    public void merge(HttpRequestMap other) {
        map.putAll(other.getMap());
    }
}
