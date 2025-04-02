package com.wolf.common.util.collection;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * com.wolf.common.util.collection
 *
 * @author Wingle
 * @since 2020/3/7 6:05 下午
 **/
public class ListUtilTest {

    @Test
    public void empty() {
    }

    @Test
    public void notEmpty() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void last() {
    }

    @Test
    public void random() {
    }

    @Test
    public void testRandom() {
        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5, 6);

        List<Integer> result = ListUtil.random(list, 2);

        assertEquals("ListUtil.random fail", 2, result.size());
    }
}