package cn.coderule.common.lang.type.map;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ObjectMapTest {

    @Test
    public void test_kv_work() {
        ObjectMap map = ObjectMap.newInstance()
                .kv("l1-1", "a")
                .kv("l1-2", "b")
                .kv("l1-3", "c");

        assertEquals("ObjectMap build fail", "a", map.getString("l1-1"));
        assertEquals("ObjectMap build fail", "b", map.getString("l1-2"));
        assertEquals("ObjectMap build fail", "c", map.getString("l1-3"));
    }

    @Test
    public void test_put_work() {
        ObjectMap map = ObjectMap.newInstance();
        map.put("l1-1", "a");

        assertEquals("put fail", "a", map.getString("l1-1"));

        map.put("l1-1", "b");
        assertEquals("put override fail", "b", map.getString("l1-1"));
    }

    @Test
    public void test_get_missing_key_returns_null() {
        ObjectMap map = ObjectMap.newInstance()
                .kv("l1-1", "a");

        assertNull(map.getString("missing"));
    }
}
