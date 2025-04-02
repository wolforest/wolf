package cn.coderule.common.util.lang.collection;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * com.wolf.common.util.collection
 *
 * @author Wingle
 * @since 2020/2/18 10:36 上午
 **/
public class SetUtil {
    public static <E> Set<E> empty(){
        return new HashSet<>();
    }

    public static <E> boolean isEmpty(Set<E> set) {
        return null == set || set.isEmpty();
    }

    public static <E> boolean notEmpty(Set<E> set) {
        return ! isEmpty(set);
    }

    public static <E> E first(Set<E> set) {
        if (isEmpty(set)) {
            return null;
        }

        return set.iterator().next();
    }

    public static <E> E last(Set<E> set) {
        if (isEmpty(set)) {
            return null;
        }

        E last = null;
        Iterator<E> iterator = set.iterator();
        while (iterator.hasNext()) {
            last = iterator.next();
        }

        return last;
    }

    public static Set<String> filterEmpty(Set<String> collection) {
        if (isEmpty(collection)) {
            return SetUtil.empty();
        }
        return collection.stream()
            .filter(s -> s != null && !s.isEmpty())
            .collect(Collectors.toSet());
    }
}
