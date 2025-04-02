package com.wolf.common.ds.map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectMapTest {

    @Test
    public void test_put_work() {
        ObjectMap map = ObjectMap.newInstance()
                .kv("l1-1", "a")
                .kv("l1-2", "b")
                .kv("l1-3", "c");

        assertEquals("ObjectTree build fail", "a", map.getString("l1-1"));
    }
}
