package com.wolf.httpclient.cookie;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import com.wolf.common.lang.exception.lang.IllegalArgumentException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MemoryCookieJar implements CookieJar {
    private final ConcurrentHashMap<String, Cookie> jar = new ConcurrentHashMap<>();

    @Override
    public void saveCookie(@NonNull Cookie cookie) {
        if (!cookie.isValid()) {
            log.error("invalid cookie: {}", cookie);
            throw new IllegalArgumentException("invalid cookie");
        }

        jar.put(cookie.getName(), cookie);
    }

    @Override
    public void saveCookies(@NonNull Collection<Cookie> cookies) {
        for (Cookie cookie : cookies) {
            saveCookie(cookie);
        }
    }

    @Override
    public List<Cookie> getCookies() {
        return new ArrayList<>(jar.values());
    }

    @Override
    public Cookie getCookie(String key) {
        return jar.get(key);
    }

    @Override
    public void removeCookie(String key) {
        if (!jar.containsKey(key)) {
            return;
        }

        jar.remove(key);
    }

    @Override
    public void clearCookies() {
        jar.clear();
    }
}
