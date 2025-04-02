package com.wolf.framework.layer.web.auth;

import com.wolf.framework.layer.web.auth.auth.Auth;
import com.wolf.framework.layer.web.auth.auth.AuthConfig;
import com.wolf.framework.layer.web.auth.model.Space;
import com.wolf.framework.layer.web.auth.model.User;
import com.wolf.framework.layer.web.auth.session.Session;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import com.wolf.framework.layer.web.filter.OncePerRequestFilter;

@Slf4j
@Getter
public class WolfAuth extends OncePerRequestFilter {
    private final Session session;
    private final Auth auth;

    public WolfAuth(RedisTemplate<String, Object> redisTemplate, AuthConfig authConfig) {
        this.session = new Session(redisTemplate, authConfig);
        this.auth = new Auth(session, authConfig);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        session.start(request, response);

        String path = request.getServletPath();
        if (!auth.isNeedAuth(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (!auth.isLogin()) {
            auth.accessDeny(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        log.error("session saved by WolfAuth");
        session.save();
    }


    /********************** proxy methods start *************************/
    public void setSessionId(String sessionId) {
        session.setSessionId(sessionId);
    }
    public <T> T get(String key, Class<T> clazz) {
        return session.get(key, clazz);
    }

    public Session set(String key, Object value) {
        return session.set(key, value);
    }

    public Long getAccountId() {
        return auth.getAccountId();
    }

    public Long getSpaceId() {
        return auth.getSpaceId();
    }

    public User getUser() {
        return auth.getUser();
    }

    public Space getSpace() {
        return auth.getSpace();
    }

    public boolean isLogin() {
        return auth.isLogin();
    }

    public boolean isLogin(Long spaceId) {
        return auth.isLogin(spaceId);
    }

    public void login(@NonNull User user) {
        auth.login(user);
    }

    public void login(@NonNull Space space) {
        auth.login(space);
    }

    public void logout() {
        auth.logout();
    }

    public void logout(Long spaceId) {
        auth.logout(spaceId);
    }

}
