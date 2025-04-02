package com.wolf.mqclient.core.message;

import com.wolf.common.util.lang.JSONUtil;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * com.wolf.mqclient.domain.converter
 *
 * @author Wingle
 * @since 2021/12/19 下午10:03
 **/
public class MessageConverter {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public static byte[] fromString(String body) {
        return body.getBytes(DEFAULT_CHARSET);
    }

    public static byte[] fromJSONObject(Object o) {
        String json = JSONUtil.toJSONString(o);
        return fromString(json);
    }

    public static String toString(byte[] bytes) {
        return new String(bytes, DEFAULT_CHARSET);
    }

    public static <T> T toJSONObject(byte[] bytes, Class<T> clazz) {
        String s = toString(bytes);
        return JSONUtil.parse(s, clazz);
    }
}
