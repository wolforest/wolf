package cn.coderule.common.util.lang.string;

import cn.coderule.common.util.lang.StringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.NonNull;
import cn.coderule.common.util.lang.collection.ArrayUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/2/26 1:00 下午
 **/
public class JSONUtil {
    public static final String EMPTY_JSON = "{}";
    public static final String KEY_SEPARATOR = ".";

    public static boolean isEmpty(JSONObject object) {
        if (object == null) {
            return true;
        }

        return object.isEmpty();
    }

    public static boolean notEmpty(JSONObject object) {
        return !isEmpty(object);
    }

    public static JSONObject parse(@NonNull String str) {
        return JSON.parseObject(str);
    }

    public static JSONArray parseArray(@NonNull String str) {
        return JSON.parseArray(str);
    }

    public static <T> T parse(@NonNull String str, Class<T> clazz) {
        return JSON.parseObject(str, clazz);
    }

    public static <T> List<T> parseArray(@NonNull String str, Class<T> clazz) {
        return JSON.parseArray(str, clazz);
    }

    public static String[] parseStringArray(@NonNull String str) {
        return JSON.parseArray(str, String.class).toArray(new String[0]);
    }

    public static JSONObject getJSONObject(@NonNull JSONObject obj, @NonNull String keyString) {
        String[] keys = StringUtil.split(keyString, KEY_SEPARATOR);
        return getJSONObject(obj, keys);
    }

    public static JSONObject getJSONObject(@NonNull JSONObject obj, String... keys) {
        if (ArrayUtil.isEmpty(keys)) {
            return null;
        }

        JSONObject tmp = obj;
        for (String key : keys) {
            tmp = tmp.getJSONObject(key);
            if (null == tmp) {
                return null;
            }
        }

        return tmp;
    }

    public static boolean hasKeys(JSONObject obj, String child, String... keys) {
        if (null == obj) {
            return false;
        }

        JSONObject tmp = obj.getJSONObject(child);
        if (tmp == null) {
            return false;
        }

        return hasKeys(tmp, keys);
    }

    public static boolean hasKeys(JSONObject obj, String... keys) {
        if (obj == null) {
            return false;
        }

        for (String key : keys) {
            if (StringUtil.isBlank(key)) {
                continue;
            }

            Object value = obj.get(key);
            if (null == value) {
                return false;
            }

            if (value instanceof String) {
                if (StringUtil.isBlank(value)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static String toJSONString(Object o) {
        if (o == null) {
            return "{}";
        }

        return JSON.toJSONString(o);
    }

    public static String toPlainString(String str) {
        String result = "";
        if (null == str) {
            return result;
        }

        Map<String, String> map = new HashMap<>();
        map.put("\\[", "");
        map.put("\\{", "");
        map.put("\\}", "");
        map.put("\\]", "");
        map.put("\"", "");
        map.put("\\\\", "");
        map.put("'", "");
        map.put(",", " ");
        result = StringUtil.replace(str, map);

        return result;
    }

}
