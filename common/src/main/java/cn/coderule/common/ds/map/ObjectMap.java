package cn.coderule.common.ds.map;

import cn.coderule.common.convention.container.Context;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cn.coderule.common.util.lang.collection.CollectionUtil;
import cn.coderule.common.util.lang.string.JSONUtil;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * com.wolf.common.lang.ds
 *
 * @author Wingle
 * @since 2020/2/29 5:20 下午
 **/
public class ObjectMap extends JSONObject implements Map<String, Object>, Context, Serializable {
    public static ObjectMap newInstance() {
        return new ObjectMap();
    }

    public ObjectMap() {
    }

    public ObjectMap(Map<String, Object> map) {
        super(map);
    }

    public String toJSONString() {
        return JSON.toJSONString(this);
    }

    public Map<String, Object> toMap() {
        return this;
    }

    public ObjectMap kv(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public <T> ObjectMap addToCollection(String key, T value) {
        CollectionUtil.add(get(key), value);
        return this;
    }

    public JSONObject getJSONObject(String... keys) {
        return JSONUtil.getJSONObject(this, keys);
    }

    public <T> T getObject(String path, String key, Class<T> clazz) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.getObject(key, clazz);
    }

    public Object get(String path, String key) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.get(key);
    }

    public Integer getInteger(String path, String key) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.getInteger(key);
    }

    public Boolean getBoolean(String path, String key) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.getBoolean(key);
    }

    public Long getLong(String path, String key) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.getLong(key);
    }

    public BigDecimal getBigDecimal(String path, String key) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.getBigDecimal(key);
    }

    public String getString(String path, String key) {
        JSONObject obj = JSONUtil.getJSONObject(this, path);
        if (obj == null) {
            return null;
        }

        return obj.getString(key);
    }


}
