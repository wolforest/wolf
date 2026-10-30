package cn.coderule.common.convention.container;

import cn.coderule.common.lang.exception.lang.ClassNotFoundException;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.Setter;

public class ApplicationContext implements Context {
    public static final String DEFAULT_NAMESPACE = "DEFAULT";

    @Getter @Setter
    private boolean throwOnNotFound;
    @Getter @Setter
    private String defaultNamespace;
    /**
     * context map:
     *   namespace ->  key -> [Object, Object, ...]
     */
    private final ConcurrentMap<String, ConcurrentMap<String, Set<Object>>> contextMap;

    public ApplicationContext() {
        this(DEFAULT_NAMESPACE);
    }

    public ApplicationContext(String defaultNamespace) {
        this.contextMap = new ConcurrentHashMap<>();
        this.defaultNamespace = defaultNamespace;
        this.throwOnNotFound = true;
    }

    public ApplicationContext throwOnNotFound(boolean throwOnNotFound) {
        this.throwOnNotFound = throwOnNotFound;
        return this;
    }

    public void registerAll(Map<String, Set<Object>> beanMap) {
        registerAll(beanMap, defaultNamespace);
    }

    public void registerAll(Map<String, Set<Object>> beanMap, String namespace) {
        ConcurrentMap<String, Set<Object>> targetMap = getBeanMap(namespace);
        targetMap.putAll(beanMap);
    }

    public void register(Object o) {
        register(o, defaultNamespace);
    }

    public void register(Object o, String namespace) {
        addBean(o.getClass().getName(), o, namespace);
    }

    public void register(Object o, Class<?> clazz) {
        register(o, clazz, defaultNamespace);
    }

    public void register(Object o, Class<?> clazz, String namespace) {
        addBean(clazz.getName(), o, namespace);
    }

    public <T> T getBean(Class<T> clazz) {
        return getBean(clazz, defaultNamespace);
    }

    public <T> T getBean(Class<T> clazz, String namespace) {
        ConcurrentMap<String, Set<Object>> namespaceMap = contextMap.get(normalize(namespace));
        Set<Object> set = null == namespaceMap ? null : namespaceMap.get(clazz.getName());

        if (CollectionUtil.isEmpty(set)) {
            return notFound(clazz);
        }

        Object bean = set.iterator().next();
        if (clazz.isInstance(bean)) {
            return clazz.cast(bean);
        }

        return notFound(clazz);
    }

    public ConcurrentMap<String, Set<Object>> getBeanMap() {
        return getBeanMap(defaultNamespace);
    }

    public ConcurrentMap<String, Set<Object>> getBeanMap(String namespace) {
        return contextMap.computeIfAbsent(
            normalize(namespace),
            _ -> new ConcurrentHashMap<>()
        );
    }

    public void merge(ApplicationContext subContext) {
        merge(subContext, defaultNamespace);
    }

    public void merge(ApplicationContext subContext, String namespace) {
        ConcurrentMap<String, Set<Object>> subMap = subContext.getBeanMap();
        if (MapUtil.isEmpty(subMap)) {
            return;
        }

        ConcurrentMap<String, Set<Object>> targetMap = getBeanMap(namespace);
        subMap.forEach((className, beans) -> targetMap.computeIfAbsent(
            className,
            _ -> ConcurrentHashMap.newKeySet()
        ).addAll(beans));
    }


    private <T> T notFound(Class<T> clazz) {
        if (!throwOnNotFound) {
            return null;
        }

        throw new ClassNotFoundException(clazz.getName());
    }

    private void addBean(String className, Object bean, String namespace) {
        ConcurrentMap<String, Set<Object>> namespaceMap = getBeanMap(namespace);
        namespaceMap.computeIfAbsent(
            className,
            _ -> ConcurrentHashMap.newKeySet()
        ).add(bean);
    }

    private String normalize(String namespace) {
        if (StringUtil.isBlank(namespace)) {
            throw new IllegalArgumentException("namespace can't be blank");
        }

        return namespace.trim();
    }

}
