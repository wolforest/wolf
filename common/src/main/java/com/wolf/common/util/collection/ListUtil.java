package com.wolf.common.util.collection;

import com.wolf.common.lang.exception.BusinessException;

import java.security.SecureRandom;
import java.util.*;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2020/1/17 9:15 下午
 **/
public class ListUtil {
    public static <E> List<E> empty(){
        return new ArrayList<>();
    }

    public static <E> boolean notEmpty(List<E> list) {
        return null != list && !list.isEmpty();
    }

    public static <E> boolean isEmpty(List<E> list) {
        return ! notEmpty(list);
    }

    public static <E> E first(List<E> list) {
        if (!notEmpty(list)) {
            return null;
        }

        return list.get(0);
    }

    public static <E> E last(List<E> list) {
        if (!notEmpty(list)) {
            return null;
        }

        int lastIndex = list.size() - 1;
        return list.get(lastIndex);
    }


    public static <E> E random(List<E> list) {
        if (isEmpty(list)) {
            return null;
        }

        int index = new Random().nextInt(list.size());
        return list.get(index);
    }

    public static <E> List<E> random(List<E> list, int num) {
        List<E> result = new ArrayList<>();
        if (num < 1 || isEmpty(list)) {
            return result;
        }

        int index, size = list.size();
        Map<Integer, Boolean> indexMap = new HashMap<>(size);
        Random random;
        try {
            random = SecureRandom.getInstanceStrong();
        } catch (Exception e) {
            throw new BusinessException("get SecureRandom error");
        }

        while (num > 0) {

            index = random.nextInt(size);
            if (null  != indexMap.get(index)) {
                continue;
            }

            indexMap.put(index, true);
            result.add(list.get(index));
            num--;
        }

        return result;
    }
}
