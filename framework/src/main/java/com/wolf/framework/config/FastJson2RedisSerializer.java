package com.wolf.framework.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.wolf.common.util.collection.ArrayUtil;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

@Slf4j
public class FastJson2RedisSerializer<T> implements RedisSerializer<T> {
    //TODO: use config
    static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(
        "com.wolf", "com.one"
    );
    private final Class<T> clazz;

    public FastJson2RedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (Objects.isNull(t)) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(t, JSONWriter.Feature.WriteClassName);
        } catch (Exception e) {
            log.error("Fastjson2 serialize error：{}", e.getMessage());
            throw new SerializationException("Can't serialize : " + e.getMessage(), e);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            return JSON.parseObject(bytes, clazz, AUTO_TYPE_FILTER);
        } catch (Exception e) {
            log.error("Fastjson2 deserialize error ：{}", e.getMessage());
            throw new SerializationException("Can't deserialize :" + e.getMessage(), e);
        }
    }
}
