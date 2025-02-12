package cn.coderule.httpclient.cookie;

import cn.coderule.httpclient.HttpClientConfig;
import cn.coderule.httpclient.HttpRequestBuilder;
import cn.coderule.httpclient.Response;
import lombok.Data;
import cn.coderule.common.util.lang.collection.CollectionUtil;

import java.util.Collection;
import java.util.List;

@Data
public class CookieManager implements CookieJar {
    private HttpClientConfig clientConfig;

    private CookiePolicyEnum policy;
    private String jarName;
    private CookieJar cookieJar;

    public CookieManager(HttpClientConfig config) {
        this.clientConfig = config;
        policy = config.getCookiePolicy();

        //TODO: load cookieJar by jarName
        jarName = config.getCookieJar();
        cookieJar = new MemoryCookieJar();
    }

    /**
     * load stored cookies and add cookies to request
     *
     * @param requestBuilder request
     */
    public void loadCookies(HttpRequestBuilder requestBuilder) {
        List<Cookie> cookies = getCookies();
        List<Cookie> cookiesSend = CookiePolicy.match(getPolicy(), requestBuilder, cookies);

        requestBuilder.addCookies(cookiesSend);
    }

    /**
     * save cookie from response
     *
     * @param requestBuilder contains cookie settings of this request
     * @param response       http response
     */
    public void saveCookies(HttpRequestBuilder requestBuilder, Response response) {
        if (CollectionUtil.isEmpty(response.getCookies())) {
            return;
        }

        List<Cookie> cookiesAccept = CookiePolicy.match(getPolicy(), requestBuilder, response.getCookies());
        cookieJar.saveCookies(cookiesAccept);
    }

    @Override
    public void saveCookie(Cookie cookie) {
        cookieJar.saveCookie(cookie);
    }

    @Override
    public void saveCookies(Collection<Cookie> cookies) {
        if (CollectionUtil.isEmpty(cookies)) {
            return;
        }

        cookieJar.saveCookies(cookies);
    }

    @Override
    public List<Cookie> getCookies() {
        return cookieJar.getCookies();
    }

    @Override
    public Cookie getCookie(String key) {
        return cookieJar.getCookie(key);
    }

    @Override
    public void removeCookie(String key) {
        cookieJar.removeCookie(key);
    }

    @Override
    public void clearCookies() {
        cookieJar.clearCookies();
    }


    private CookiePolicyEnum getPolicy(HttpRequestBuilder request) {
        if (null != request.getCookiePolicy()) {
            return request.getCookiePolicy();
        }

        return policy;
    }
}
