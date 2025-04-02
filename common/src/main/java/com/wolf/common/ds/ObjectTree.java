package com.wolf.common.ds;

import com.alibaba.fastjson2.util.TypeUtils;
import com.wolf.common.ds.map.ObjectMap;
import lombok.NonNull;
import com.wolf.common.lang.exception.lang.IllegalArgumentException;

public class ObjectTree {
    private final ObjectMap root;
    private ObjectMap node;

    public static ObjectTree newInstance() {
        return new ObjectTree();
    }

    public ObjectTree() {
        root = new ObjectMap();
        node = root;
    }

    public <T> T getObject(@NonNull String key, @NonNull Class<T> clazz) {
        return root.getObject(key, clazz);
    }

    public <T> T getObject(@NonNull Class<T> clazz, String... keys) {
        Object v = get(keys);
        if (v == null) {
            return null;
        }

        return TypeUtils.cast(v, clazz);
    }

    public Object get(@NonNull String... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("keys can't be empty");
        }

        ObjectMap node = root;
        String key;

        for (int i = 0; i < keys.length - 1; i++) {
            key = keys[i];
            node = node.getObject(key, ObjectMap.class);
            if (node == null) {
                return null;
            }
        }

        String lastKey = keys[keys.length - 1];
        return node.get(lastKey);
    }

    public ObjectTree put(String key, Object value) {
        node.put(key, value);
        return this;
    }

    public ObjectTree children(String... keys) {
        for (String k : keys) {
            findNode(k);
        }

        return this;
    }

    public ObjectTree root() {
        node = root;
        return this;
    }

    public ObjectTree parent(String... keys) {
        if (keys.length == 0) {
            throw new IllegalArgumentException("parent can't be empty");
        }

        node = root;
        children(keys);

        return this;
    }

    public String toJSONString() {
        return root.toJSONString();
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    private void findNode(String key) {
        Object tmp = node.get(key);
        if (null != tmp && !(tmp instanceof ObjectMap)) {
            throw new IllegalArgumentException("this key already set: " + key);
        }

        if (null != tmp) {
            node = (ObjectMap) tmp;
        } else {
            node.put(key, new ObjectMap());
            node = node.getObject(key, ObjectMap.class);
        }
    }


}
