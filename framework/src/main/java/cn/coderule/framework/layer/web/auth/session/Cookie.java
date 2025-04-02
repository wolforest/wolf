package cn.coderule.framework.layer.web.auth.session;

import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.framework.layer.web.auth.auth.AuthConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class Cookie {
    private final HttpServletRequest servletRequest;
    private final HttpServletResponse servletResponse;
    private final AuthConfig authConfig;

    public Cookie(HttpServletRequest servletRequest, HttpServletResponse servletResponse, AuthConfig authConfig) {
        this.authConfig = authConfig;
        this.servletRequest = servletRequest;
        this.servletResponse = servletResponse;
    }

    public void set(String key, String value) {
        set(key, value,false);
    }

    public void set(String key, String value, boolean isSecure) {
        jakarta.servlet.http.Cookie cookie = new jakarta.servlet.http.Cookie(key, value);
        cookie.setSecure(isSecure);

        if (StringUtil.notBlank(authConfig.getDomain())) {
            cookie.setDomain(authConfig.getDomain());
        }
        cookie.setPath(authConfig.getPath());
        cookie.setMaxAge(authConfig.getCookieMaxAge());
        cookie.setHttpOnly(authConfig.isHttpOnly());

        this.servletResponse.addCookie(cookie);
    }

    public String get(String key) {
        jakarta.servlet.http.Cookie cookie = getCookie(key);
        if (cookie != null) {
            return cookie.getValue();
        }
        return null;
    }

    private jakarta.servlet.http.Cookie getCookie(String key) {
        jakarta.servlet.http.Cookie[] arrCookie = this.servletRequest.getCookies();
        if (arrCookie == null) {
            return null;
        }

        for (jakarta.servlet.http.Cookie cookie : arrCookie) {
            if (cookie.getName().equals(key)) {
                return cookie;
            }
        }
        return null;
    }


    public void remove(String key) {
        jakarta.servlet.http.Cookie cookie = getCookie(key);
        if (cookie != null) {
            set(key, "", false);
        }
    }

}
