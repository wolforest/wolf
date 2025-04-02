package cn.coderule.common.util.lang.collection;

import com.google.common.base.Joiner;
import lombok.NonNull;
import cn.coderule.common.lang.exception.lang.IllegalArgumentException;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.util.CollectionUtils;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2020/1/14 3:58 下午
 **/
public class CollectionUtil {
    public static <E> void checkNotEmpty(Collection<E> collection) {
        if (notEmpty(collection)) {
            return;
        }

        throw new IllegalArgumentException("collection can't be empty");
    }

    public static <E> List<E> empty() {
        return new ArrayList<>();
    }

    public static <E> List<E> sub(Collection<E> collection, int num) {
        if (isEmpty(collection)) {
            return ListUtil.empty();
        }

        if (collection.size() <= num) {
            return new ArrayList<>(collection);
        }

        List<E> list = new ArrayList<>(num);
        int count = 0;
        for (E e : collection) {
            if (count >= num) {
                break;
            }

            list.add(e);
            count++;
        }

        return list;
    }

    public static <E> boolean notEmpty(Collection<E> collection) {
        return null != collection && !collection.isEmpty();
    }

    public static <E> boolean isEmpty(Collection<E> collection) {
        return !notEmpty(collection);
    }

    public static <E> E first(Collection<E> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        for (E e : collection) {
            return e;
        }

        return null;
    }

    public static <E> E firstNotNull(Collection<E> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        for (E e : collection) {
            if (null != e) {
                return e;
            }
        }

        return null;
    }

    public static <E> E last(Collection<E> collection) {
        if (isEmpty(collection)) {
            return null;
        }

        E target = null;
        for (E e : collection) {
            target = e;
        }

        return target;
    }

    @SuppressWarnings("unchecked")
    public static <T> void add(Object obj, T t) {
        if (!(obj instanceof Collection)) {
            throw new IllegalArgumentException("obj is not collection");
        }

        try {
            ((Collection<Object>) obj).add(t);
        } catch (Exception e) {
            throw new IllegalArgumentException("collection type doesn't match");
        }
    }

    public static <C> List<C> filter(Collection<C> collection, Predicate<? super C> predicate) {
        if (isEmpty(collection)) {
            return ListUtil.empty();
        }
        return collection.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    public static <C> Optional<C> filterFirst(Collection<C> collection, Predicate<? super C> predicate) {
        if (isEmpty(collection)) {
            return Optional.empty();
        }
        return collection.stream()
                .filter(predicate)
                .findFirst();
    }

    public static <C> Optional<C> filterAny(Collection<C> collection, Predicate<? super C> predicate) {
        if (isEmpty(collection)) {
            return Optional.empty();
        }
        return collection.stream()
                .filter(predicate)
                .findAny();
    }

    public static <C, K> List<K> keys(Collection<C> collection, Function<C, K> getter) {
        if (isEmpty(collection)) {
            return ListUtil.empty();
        }

        return collection.stream()
                .map(getter)
                .collect(Collectors.toList());
    }

    public static <C> Map<C, Boolean> map(Collection<C> collection) {
        Map<C, Boolean> map = new HashMap<>();
        if (isEmpty(collection)) {
            return map;
        }

        for (C c : collection) {
            map.put(c, true);
        }

        return map;
    }

    public static <C, K> Map<K, C> map(Collection<C> collection, Function<C, K> getter) {
        return collection.stream().collect(
                Collectors.toMap(getter, Function.identity())
        );
    }

    public static <C, K, V> Map<K, V> map(Collection<C> collection, Function<C, K> keyGetter, Function<C, V> valueGetter) {
        return collection.stream().collect(
                Collectors.toMap(keyGetter, valueGetter)
        );
    }

    public static <S extends Number, E> long sum(Collection<E> collection, Function<E, S> getter) {
        if (collection == null || null == getter || collection.isEmpty()) {
            return 0;
        }

        long result = 0;
        for (E e : collection) {
            S s = getter.apply(e);
            result += s.longValue();
        }

        return result;
    }

    public static <S extends Number, G, E> Map<G, Long> groupAndSum(Collection<E> collection, Function<E, G> grouper, Function<E, S> getter) {
        Map<G, List<E>> map = group(collection, grouper);
        if (map == null) {
            return null;
        }

        Map<G, Long> result = new HashMap<>();
        for (Map.Entry<G, List<E>> entry : map.entrySet()) {
            G key = entry.getKey();
            List<E> value = entry.getValue();

            result.put(key, sum(value, getter));
        }

        return result;
    }

    public static <G, E> Map<G, List<E>> group(Collection<E> collection, Function<E, G> getter) {
        if (collection == null || null == getter || collection.isEmpty()) {
            return null;
        }

        Map<G, List<E>> map = new HashMap<>();
        for (E c : collection) {
            G groupKey = getter.apply(c);
            List<E> list = map.get(groupKey);
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(c);
            map.put(groupKey, list);
        }

        return map;
    }

    public static <C1, MERGE_TYPE, C2> void join(Collection<C1> c1, Function<C1, MERGE_TYPE> getter, BiConsumer<C1, C2> setter, Collection<C2> c2, Function<C2, MERGE_TYPE> mGetter) {
        Map<MERGE_TYPE, C2> map = map(c2, mGetter);
        join(c1, getter, setter, map);
    }

    public static <C, MERGE_TYPE, M> void join(Collection<C> collection, Function<C, MERGE_TYPE> getter, BiConsumer<C, M> setter, Map<MERGE_TYPE, M> map) {
        for (C c : collection) {
            MERGE_TYPE k = getter.apply(c);
            M v = map.get(k);
            setter.accept(c, v);
        }
    }

    public static <Source, Target> List<Target> to(Collection<Source> sources, Function<Source, Target> convert) {
        if (sources == null || null == convert || sources.isEmpty()) {
            return ListUtil.empty();
        }

        List<Target> result = new ArrayList<>();
        for (Source source : sources) {
            Target target = convert.apply(source);
            result.add(target);
        }

        return result;
    }

    public static <C> String join(@NonNull String delimiter, @NonNull Collection<C> collection) {
        if (collection.isEmpty()) {
            return "";
        }

        Joiner joiner = Joiner.on(delimiter).skipNulls();
        return joiner.join(collection);
    }

    public static <E> void merge(Collection<E> source, @NonNull Collection<E> target) {
        if (isEmpty(source)) {
            return;
        }

        for (E o : source) {
            if (target.contains(o)) {
                continue;
            }

            target.add(o);
        }
    }

    /**
     * check if child is sub of parent
     * - any vs null => false
     * - any vs empty => false
     * - empty vs not empty => true
     * - x vs item => true
     *
     * @param parent parent
     * @param child child
     * @return true if child is sub of parent
     */
    public static <E> boolean isSub(Collection<E> parent, Collection<E> child) {
        if (isEmpty(child)) {
            return false;
        }

        if (isEmpty(parent)) {
            return false;
        }

        HashSet<E> set = new HashSet<>(parent);
        for (E o : child) {
            if (!set.contains(o)) {
                return false;
            }
        }

        return true;
    }

    /**
     * check if source and target is different
     * @param source source
     * @param target target
     * @return true if different
     */
    public static <E> boolean isDifferent(Collection<E> source, Collection<E> target) {
        if (null == source) {
            return null != target;
        }

        if (null == target) {
            return true;
        }

        if (source.isEmpty() && target.isEmpty()) {
            return false;
        }

        if (source.size() != target.size()) {
            return true;
        }

        HashSet<E> set = new HashSet<>(source);
        for (E o : target) {
            if (!set.contains(o)) {
                return true;
            }
        }

        return false;
    }
}
