package com.wolf.common.util.collection;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * com.wolf.common.util.collection
 *
 * @author Wingle
 * @since 2021/12/13 下午10:38
 **/
public class SetUtilTest {

    @Test
    public void first() {
        Set<String> stringSet = createSet();

        assertEquals("SetUtil.first fail", "1", SetUtil.first(stringSet));
    }

    @Test
    public void last() {
        Set<String> stringSet = createSet();

        assertEquals("SetUtil.last fail", "5", SetUtil.last(stringSet));
    }

    private Set<String> createSet() {
        Set<String> examples = new HashSet<>();
        examples.add("1");
        examples.add("2");
        examples.add("3");
        examples.add("4");
        examples.add("5");

        return examples;
    }
}