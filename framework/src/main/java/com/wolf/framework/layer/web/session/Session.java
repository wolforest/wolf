package com.wolf.framework.layer.web.session;

import com.wolf.common.ds.map.ObjectMap;
import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.common.util.lang.StringUtil;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextListener;

public class Session extends RequestContextListener {
    private static final String DEFAULT_NAMESPACE = "default";

    private final SessionConfig sessionConfig;
    private final RedisTemplate<String, ObjectMap> redisTemplate;
    private final HashOperations<String, String, ObjectMap> redisHash;

    private Cookie cookie;
    private String sessionId;

    private Map<String, ObjectMap> sessionData;
    private Set<String> changedNamespaces;


    public Session(RedisTemplate<String, ObjectMap> redisTemplate, SessionConfig sessionConfig) {
        this.redisTemplate = redisTemplate;
        this.sessionConfig = sessionConfig;
        this.redisHash = redisTemplate.opsForHash();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent requestEvent) {
        super.requestDestroyed(requestEvent);
        this.save();
    }

    public void start(HttpServletRequest request, HttpServletResponse response) {
        this.cookie = new Cookie(request, response, this.sessionConfig);
        this.sessionData = new HashMap<>();

        this.getOrCreateSessionId();
        this.loadSession(DEFAULT_NAMESPACE);

        this.changedNamespaces = new HashSet<>();
    }

    public <T> T get(String key, Class<T> clazz) {
        return get(DEFAULT_NAMESPACE, key, clazz);
    }

    public <T> T get(String namespace, String key, Class<T> clazz) {
        ObjectMap data = getNamespace(namespace);
        if (data == null) {
            return null;
        }

        return data.getObject(key, clazz);
    }

    public ObjectMap getNamespace(String namespace) {
        ObjectMap result = this.sessionData.get(namespace);
        if (result != null) {
            return result;
        }
        return loadSession(namespace);
    }

    public Session setNamespace(String namespace, ObjectMap map) {
        this.sessionData.put(namespace, map);
        changedNamespaces.add(namespace);
        return this;
    }

    public Session set(String key, Object value) {
        return set(DEFAULT_NAMESPACE, key, value);
    }

    public Session set(String namespace, String key, Object value) {
        ObjectMap data = getNamespace(namespace);
        if (data == null) {
            data = new ObjectMap();
            sessionData.put(namespace, data);
        }

        data.put(key, value);
        changedNamespaces.add(namespace);

        return this;
    }

    public void save() {
        if (CollectionUtil.isEmpty(changedNamespaces)) {
            return ;
        }

        Map<String, ObjectMap> data = new HashMap<>();
        for (String namespace : changedNamespaces) {
            data.put(namespace, sessionData.get(namespace));
        }

        redisHash.putAll(sessionId, data);
        redisTemplate.expire(sessionId, sessionConfig.getSessionMaxAge(), TimeUnit.SECONDS);
    }

    private void getOrCreateSessionId() {
        String cookieKey = cookie.get(sessionConfig.getCookieKey());
        if (StringUtil.isBlank(cookieKey)) {
            cookieKey = StringUtil.uuid();
            cookie.set(sessionConfig.getCookieKey(), cookieKey, true);
        }
        this.sessionId = cookieKey;
    }

    private ObjectMap loadSession(String namespace) {
        ObjectMap result = this.sessionData.get(namespace);
        if (null != result) {
            return result;
        }

        result = redisHash.get(this.sessionId, namespace);
        if (result == null) {
            result = new ObjectMap();
        }

        sessionData.put(namespace, result);
        return result;
    }

}