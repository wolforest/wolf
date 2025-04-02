package cn.coderule.framework.layer.web.auth.session;

import cn.coderule.common.ds.map.ObjectMap;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.StringUtil;
import cn.coderule.framework.layer.web.auth.auth.AuthConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
public class Session {
    private static final String DEFAULT_NAMESPACE = "default";

    private final AuthConfig authConfig;
    private final RedisTemplate<String, Object> redisTemplate;
    private final HashOperations<String, String, ObjectMap> redisHash;


    private Cookie cookie;
    private String sessionId;

    private Map<String, ObjectMap> sessionData;
    private Set<String> changedNamespaces;

    public Session(RedisTemplate<String, Object> redisTemplate, AuthConfig authConfig) {
        this.redisTemplate = redisTemplate;
        this.authConfig = authConfig;
        this.redisHash = redisTemplate.opsForHash();
    }

    public void start(HttpServletRequest request, HttpServletResponse response) {
        this.cookie = new Cookie(request, response, this.authConfig);
        this.sessionData = new HashMap<>();

        this.getOrCreateSessionId(request);
        this.loadSession(DEFAULT_NAMESPACE);

        this.changedNamespaces = new HashSet<>();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public void destroy() {
        redisTemplate.delete(this.sessionId);
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
        redisTemplate.expire(sessionId, authConfig.getSessionMaxAge(), TimeUnit.SECONDS);
    }

    private void getOrCreateSessionId(HttpServletRequest request) {
        String bearer = getBearer(request);
        if (StringUtil.notBlank(bearer)) {
            this.sessionId = bearer;
            return;
        }

        this.initSessionIdByCookie();
    }

    private void initSessionIdByCookie() {
        String cookieKey = cookie.get(authConfig.getCookieKey());
        if (StringUtil.isBlank(cookieKey)) {
            cookieKey = StringUtil.uuid();
            cookie.set(authConfig.getCookieKey(), cookieKey, true);
        }
        this.sessionId = cookieKey;
    }

    private String getBearer(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (StringUtil.isEmpty(auth)) {
            return null;
        }

        String[] authArr = auth.split(" ");
        if (authArr.length != 2 || !"Bearer".equals(authArr[0])) {
            return null;
        }

        if (StringUtil.isEmpty(authArr[1])) {
            return null;
        }

        return authArr[1].trim();
    }

    private ObjectMap loadSession(String namespace) {
        ObjectMap result = this.sessionData.get(namespace);
        if (null != result) {
            return result;
        }

        result = redisHash.get(this.sessionId, namespace);
        if (result == null) {
            log.info("load session, namespace: {}, sessionId: {}, data: {}, redisTemplate: {}", namespace, sessionId, result, redisTemplate);
            result = new ObjectMap();
        }

        sessionData.put(namespace, result);
        return result;
    }
}
