package cn.coderule.httpclient.cookie;

import java.util.Collection;
import java.util.List;

public interface CookieJar {
    void saveCookie(Cookie cookie);

    void saveCookies(Collection<Cookie> cookies);

    List<Cookie> getCookies();

    Cookie getCookie(String key);

    void removeCookie(String key);

    void clearCookies();
}
