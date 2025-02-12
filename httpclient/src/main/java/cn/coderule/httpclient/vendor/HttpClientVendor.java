package cn.coderule.httpclient.vendor;

import cn.coderule.httpclient.HttpRequestBuilder;
import cn.coderule.httpclient.Response;
import cn.coderule.httpclient.HttpClientConfig;
import cn.coderule.httpclient.cookie.CookieManager;

public interface HttpClientVendor {
    void setClientConfig(HttpClientConfig config);

    void setCookieManager(CookieManager cookieManager);

    Response execute(HttpRequestBuilder requestBuilder);
}
