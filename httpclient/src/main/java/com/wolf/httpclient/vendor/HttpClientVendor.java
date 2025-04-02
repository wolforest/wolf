package com.wolf.httpclient.vendor;

import com.wolf.httpclient.HttpClientConfig;
import com.wolf.httpclient.HttpRequestBuilder;
import com.wolf.httpclient.Response;
import com.wolf.httpclient.cookie.CookieManager;

public interface HttpClientVendor {
    void setClientConfig(HttpClientConfig config);

    void setCookieManager(CookieManager cookieManager);

    Response execute(HttpRequestBuilder requestBuilder);
}
