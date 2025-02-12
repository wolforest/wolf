package cn.coderule.httpclient.vendor.netty;

import io.netty.handler.codec.http.cookie.CookieHeaderNames;
import io.netty.handler.codec.http.cookie.DefaultCookie;
import org.springframework.beans.BeanUtils;
import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.httpclient.cookie.Cookie;
import cn.coderule.httpclient.cookie.InvalidCookieException;
import cn.coderule.httpclient.cookie.SameSiteEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CookieConverter {
    public static DefaultCookie from(Cookie c) {
        if (!c.isValid()) {
            throw new InvalidCookieException();
        }

        DefaultCookie defaultCookie = new DefaultCookie(c.getName(), c.getValue());
        BeanUtils.copyProperties(c, defaultCookie);

        defaultCookie.setSameSite(from(c.getSameSite()));
        return defaultCookie;
    }

    public static List<Cookie> to(Map<CharSequence, Set<io.netty.handler.codec.http.cookie.Cookie>> responseCookies) {
        List<Cookie> cookies = new ArrayList<>();
        if (MapUtil.isEmpty(responseCookies)) {
            return cookies;
        }

        for (Set<io.netty.handler.codec.http.cookie.Cookie> responseCookieSet : responseCookies.values()) {
            for (io.netty.handler.codec.http.cookie.Cookie responseCookie : responseCookieSet) {
                cookies.add(to(responseCookie));
            }
        }

        return cookies;
    }

    public static Cookie to(io.netty.handler.codec.http.cookie.Cookie c) {
        Cookie cookie = new Cookie();
        //BeanUtils.copyProperties(c, cookie);
        cookie.setName(c.name());
        cookie.setValue(c.value());
        cookie.setDomain(c.domain());
        cookie.setPath(c.path());
        cookie.setMaxAge(c.maxAge());
        cookie.setSecure(c.isSecure());
        cookie.setHttpOnly(c.isHttpOnly());
        //cookie.setExpires(LocalDateTime.now().plusSeconds(c.maxAge()));

        return cookie;
    }

    public static CookieHeaderNames.SameSite from(SameSiteEnum siteEnum) {
        if (null == siteEnum) {
            return null;
        }

        switch (siteEnum) {
            case LAX:
                return CookieHeaderNames.SameSite.Lax;
            case NONE:
                return CookieHeaderNames.SameSite.None;
            case STRICT:
                return CookieHeaderNames.SameSite.Strict;
        }

        return null;
    }

    public static SameSiteEnum to(CookieHeaderNames.SameSite siteEnum) {
        if (null == siteEnum) {
            return null;
        }

        switch (siteEnum) {
            case Lax:
                return SameSiteEnum.LAX;
            case None:
                return SameSiteEnum.NONE;
            case Strict:
                return SameSiteEnum.STRICT;
        }

        return null;
    }
}
