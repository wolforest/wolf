package cn.coderule.common.lang.type.map;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ObjectMapTest {

    @Test
    public void test_kv_work() {
        ObjectMap map = ObjectMap.newInstance()
                .kv("l1-1", "a")
                .kv("l1-2", "b")
                .kv("l1-3", "c");

        assertEquals("a", map.getString("l1-1"), "ObjectMap build fail");
        assertEquals("b", map.getString("l1-2"), "ObjectMap build fail");
        assertEquals("c", map.getString("l1-3"), "ObjectMap build fail");
    }

    @Test
    public void test_put_work() {
        ObjectMap map = ObjectMap.newInstance();
        map.put("l1-1", "a");

        assertEquals("a", map.getString("l1-1"), "put fail");

        map.put("l1-1", "b");
        assertEquals("b", map.getString("l1-1"), "put override fail");
    }

    @Test
    public void test_get_missing_key_returns_null() {
        ObjectMap map = ObjectMap.newInstance()
                .kv("l1-1", "a");

        assertNull(map.getString("missing"));
    }
}
