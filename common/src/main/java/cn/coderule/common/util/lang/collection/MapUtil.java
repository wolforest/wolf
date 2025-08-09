package cn.coderule.common.util.lang.collection;

import cn.coderule.common.util.lang.bean.BeanUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import com.google.common.collect.Maps;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * com.wolf.common.util.collection
 *
 * @author Wingle
 * @since 2020/2/17 5:55 下午
 **/
public class MapUtil {

    public static <K, V> HashMap<K, V> newHashMapWithExpectedSize(int expectedSize) {
        return Maps.newHashMapWithExpectedSize(expectedSize);
    }

    public static <K, V> Map<K, V> empty() {
        return new HashMap<>();
    }

    public static <K, V> boolean notEmpty(Map<K, V> map) {
        return notEmpty(map, false);
    }

    public static <K, V> boolean notEmpty(Map<K, V> map, boolean filterNulls) {
        if (map == null) {
            return false;
        }

        if (map.isEmpty()) {
            return false;
        }

        if (!filterNulls) {
            return true;
        }

        for (Map.Entry<K, V> entry : map.entrySet()) {
            if (null != entry.getKey() && null != entry.getValue()) {
                return true;
            }
        }

        return false;
    }

    public static <K, V> boolean isEmpty(Map<K, V> map, boolean filterNulls) {
        return ! notEmpty(map, filterNulls);
    }

    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return ! notEmpty(map);
    }

    public static int calculateLength(Map<String, String> map) {
        return calculateLength(map, StandardCharsets.UTF_8);
    }

    public static int calculateLength(Map<String, String> map, Charset charset) {
        if (isEmpty(map)) {
            return 0;
        }

        int size = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            size += entry.getKey().getBytes(charset).length;
            size += entry.getValue().getBytes(charset).length;
        }

        return size;
    }

    public static <K, V> List<V> toList(Map<K, V> map) {
        if (isEmpty(map)) {
            return ListUtil.empty();
        }

        return new ArrayList<>(map.values());
    }

    public static <K, V> boolean containsNull(@NonNull Map<K, V> map) {
        return map.containsKey(null) || map.containsValue(null);
    }

    public static <K, V> Map<K, V> subMap(Map<K, V> map, Collection<K> keys) {
        return subMap(map, keys, false);
    }

    public static <K, V> Map<K, V> subMap(Map<K, V> map, Collection<K> keys, boolean skipNulls) {
        if (CollectionUtil.isEmpty(keys)) {
            return empty();
        }

        Map<K, V> result = new HashMap<>();
        V v;
        for (K k : keys) {
            v = map.get(k);

            if (null == v && skipNulls) {
                continue;
            }

            result.put(k, v);
        }

        return result;
    }

    public static <K, V> Map<K, V> exceptKeys(Map<K, V> map, List<K> keys) {
        if (isEmpty(map)) {
            return empty();
        }

        return map.entrySet()
                .stream()
                .filter(e -> !keys.contains(e.getKey()))
                .collect(HashMap::new, (newMap, entry) -> newMap.put(entry.getKey(), entry.getValue()), HashMap::putAll);
    }

    public static <K, V> Map<K, V> replaceValues(Map<K, V> map, List<K> keys, V replace) {
        if (isEmpty(map)) {
            return empty();
        }

        return map.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, p -> keys.contains(p.getKey()) ? replace : p.getValue()));
    }

    public static <K, V> Map<K, V> filterNull(Map<K, V> map) {
        if (isEmpty(map)) {
            return empty();
        }

        return map.entrySet()
                .stream()
                .filter(e -> null != e.getValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static <K, V> Map<K, V> filterBlank(Map<K, V> map) {
        if (isEmpty(map)) {
            return empty();
        }

        return map.entrySet()
                .stream()
                .filter(e -> {
                    V val = e.getValue();
                    if (val instanceof String) {
                        return !StringUtil.isBlank(val);
                    } else {
                        return null != val;
                    }
                })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @SafeVarargs
    public static <K, V> Map<K, V> subMap(Map<K, V> map, boolean skipNulls, K... keys) {
        return subMap(map, Arrays.asList(keys), skipNulls);
    }

    @SafeVarargs
    public static <K, V> Map<K, V> subMap(Map<K, V> map, K... keys) {
        return subMap(map, Arrays.asList(keys));
    }

    public static <K, V> void remove(Map<K, V> map, Collection<K> keys) {
        if (CollectionUtil.isEmpty(keys)) {
            return ;
        }

        for (K k : keys) {
            map.remove(k);
        }
    }

    @SafeVarargs
    public static <K, V> void remove(Map<K, V> map, K... keys) {
        remove(map, Arrays.asList(keys));
    }

    public static <K, V> boolean contains(@NonNull Map<K, V>map, Collection<K> keys) {
        if (CollectionUtil.isEmpty(keys)) {
            return false;
        }

        for (K k : keys) {
            if (null == map.get(k)) {
                return false;
            }
        }

        return true;
    }

    @SafeVarargs
    public static <K, V> boolean contains(@NonNull Map<K, V>map, K... keys) {
        return contains(map, Arrays.asList(keys));
    }

    public static <K, V> Map<K, V> difference(Map<K, V> left, Map<K, V> right) {
        return Maps.difference(left, right).entriesOnlyOnLeft();
    }

    public static <K, V> void copy(Map<K, V> source, Map<K, V> target) {
        copy(source, target, false, null);
    }

    public static <K, V> void copy(Map<K, V> source, Map<K, V> target, boolean filterNulls) {
        copy(source, target, filterNulls, null);
    }

    public static <K, V> void copy(Map<K, V> source, Map<K, V> target, Collection<String> ignoreKeys) {
        copy(source, target, false, ignoreKeys);
    }

    public static <K, V> void copy(@NonNull Map<K, V> source, @NonNull Map<K, V> target, boolean filterNulls, Collection<String> ignoreKeys) {
        if (source.isEmpty()) {
            return;
        }

        V vSource, vTarget;
        for (K k : source.keySet()) {
            if (CollectionUtil.notEmpty(ignoreKeys) && ignoreKeys.contains(k.toString())) {
                continue;
            }

            vSource = source.get(k);
            vTarget = target.get(k);
            if (null == vSource || null == vTarget) {
                copyNullKvs(k, vSource, vTarget, target, filterNulls);
                continue;
            }

            if (BeanUtil.isSimpleObject(vSource)) {
                target.put(k, vSource);
                continue;
            }

            if (vSource instanceof Map) {
                copyInnerMap(k, vSource, vTarget, target, filterNulls, ignoreKeys);
                continue;
            }

            if (vSource instanceof Collection) {
                copyInnerCollection(k, vSource, vTarget, target, filterNulls);
                continue;
            }

            BeanUtil.copyProperties(vSource, vTarget, filterNulls, ignoreKeys);
        }
    }

    public static <K, V> Map<K, V> merge(@NonNull Map<K, V> map1, @NonNull Map<K, V> map2) {
        return Stream.of(map1, map2)
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (v1, v2) -> v2
            ));
    }

    private static <K, V> void copyNullKvs(K k, V vSource, V vTarget, @NonNull Map<K, V> target, boolean filterNulls) {
        if (null != vSource && null != vTarget) {
            return;
        }

        if (vTarget == null) {
            target.put(k, vSource);
            return;
        }

        if (!filterNulls) {
            target.put(k, null);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <K, V> void copyInnerMap(K k, V vSource, V vTarget, @NonNull Map<K, V> target, boolean filterNulls, Collection<String> ignoreKeys) {
        if (vTarget instanceof Map) {
            copy((Map)vSource, (Map)vTarget, filterNulls, ignoreKeys);
        } else {
            target.put(k, vSource);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <K, V> void copyInnerCollection(K k, V vSource, V vTarget, @NonNull Map<K, V> target, boolean filterNulls) {
        if (vTarget instanceof Collection && !filterNulls) {
            CollectionUtil.merge((Collection) vSource, (Collection) vTarget);
        } else {
            target.put(k, vSource);
        }
    }


}
