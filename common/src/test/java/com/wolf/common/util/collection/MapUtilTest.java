package com.wolf.common.util.collection;

import lombok.Data;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * com.wolf.common.util.collection
 *
 * @author Wingle
 * @since 2020/2/18 8:24 上午
 **/
public class MapUtilTest {

    @Test
    public void empty() {

    }

    @Test
    public void hasValue() {
    }

    @Test
    public void testHasValue() {
    }

    @Test
    public void isEmpty() {
        assertTrue("null isEmpty failed", MapUtil.isEmpty(null));
        assertTrue("emptyMap isEmpty failed", MapUtil.isEmpty(MapUtil.empty()));

        Map<String, Object> map = new HashMap<>();
        assertTrue("emptyMap isEmpty failed", MapUtil.isEmpty(map));

        map.put(null, "nullValue");
        assertTrue("null key isEmpty failed", MapUtil.isEmpty(map, true));

        map.put("nullKey", null);
        assertTrue("null value isEmpty failed", MapUtil.isEmpty(map, true));

        map.put("a", "abc");
        assertFalse("map notEmpty failed", MapUtil.isEmpty(map, true));
    }

    @Test
    public void containsNull() {
        Map<String, Object> map = new HashMap<>();
        assertFalse("containsNull failed", MapUtil.containsNull(map));

        map.put("nullKey", null);
        assertTrue("containsNull failed", MapUtil.containsNull(map));

        map.put(null, "nullValue");
        assertTrue("containsNull failed", MapUtil.containsNull(map));
    }

    @Test
    public void containsNullValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("nullKey", null);

        assertTrue("containsNullValue failed", map.containsValue(null));
    }

    @Test
    public void containsNullKey() {
        Map<String, Object> map = new HashMap<>();
        map.put(null, "nullValue");

        assertTrue("containsNullKey failed", map.containsKey(null));
    }

    @Test
    public void subMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        Map<String, Object> sub = MapUtil.subMap(map, "a", "b", "e");

        assertNotNull("subMap failed", sub);
        assertEquals("subMap failed", 1, sub.get("a"));
        assertTrue("subMap failed", sub.containsKey("e"));
        assertNull("subMap failed", sub.get("e"));
    }

    @Test
    public void subMapNullKeys() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        Map<String, Object> sub = MapUtil.subMap(map);

        assertNotNull("subMap failed", sub);
        assertTrue("subMap failed", sub.isEmpty());
    }

    @Test
    public void testSubMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        Map<String, Object> sub = MapUtil.subMap(map, true, "a", "b", "e");

        assertNotNull("subMap failed", sub);
        assertEquals("subMap failed", 1, sub.get("a"));
        assertFalse("subMap skipNulls failed", sub.containsKey("e"));
        assertNull("subMap failed", sub.get("e"));
    }

    @Test
    public void testSubMap1() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        String[] keys = new String[]{"a", "b", "e"};
        List<String> keyList = Arrays.asList(keys);

        Map<String, Object> sub = MapUtil.subMap(map, keyList);

        assertNotNull("subMap failed", sub);
        assertEquals("subMap failed", 1, sub.get("a"));
        assertTrue("subMap failed", sub.containsKey("e"));
        assertNull("subMap failed", sub.get("e"));
    }

    @Test
    public void testSubMap2() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        String[] keys = new String[]{"a", "b", "e"};
        List<String> keyList = Arrays.asList(keys);

        Map<String, Object> sub = MapUtil.subMap(map, keyList, true);


        assertNotNull("subMap failed", sub);
        assertEquals("subMap failed", 1, sub.get("a"));
        assertFalse("subMap skipNulls failed", sub.containsKey("e"));
        assertNull("subMap failed", sub.get("e"));
    }

    @Test
    public void remove() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        String[] keys = new String[]{"a", "b", "e"};
        List<String> keyList = Arrays.asList(keys);

        MapUtil.remove(map, keyList);
        assertFalse("MapUtil.remove failed", map.containsKey("a"));
        assertFalse("MapUtil.remove failed", map.containsKey("b"));
        assertFalse("MapUtil.remove failed", map.containsKey("e"));
    }

    @Test
    public void testRemove() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        MapUtil.remove(map, "a", "b", "e");
        assertFalse("MapUtil.remove failed", map.containsKey("a"));
        assertFalse("MapUtil.remove failed", map.containsKey("b"));
        assertFalse("MapUtil.remove failed", map.containsKey("e"));
    }

    @Test
    public void contains() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);
        map.put("d", 4);
        map.put("e", null);

        String[] key1 = new String[]{"a", "b", "e"};
        List<String> keyList1 = Arrays.asList(key1);
        assertFalse("MapUtil.contains failed", MapUtil.contains(map, key1));
        assertFalse("MapUtil.contains failed", MapUtil.contains(map, keyList1));

        String[] key2 = new String[]{"a", "b"};
        List<String> keyList2 = Arrays.asList(key2);
        assertTrue("MapUtil.contains failed", MapUtil.contains(map, key2));
        assertTrue("MapUtil.contains failed", MapUtil.contains(map, keyList2));


        assertTrue("MapUtil.contains failed", MapUtil.contains(map, "a", "b", "c"));
        assertFalse("MapUtil.contains failed", MapUtil.contains(map, "a", "b", "f"));
        assertFalse("MapUtil.contains failed", MapUtil.contains(map, "n", "h", "f"));
    }

    @Test
    public void merge() {
        Map<String, Channel> source = createSwitchMap(10);
        Map<String, Channel> target = createChannelMap(10);

        MapUtil.copy(source, target);
        assertNotNull("MapUtil.merge fail", target);
        assertTrue("MapUtil.merge fail", target.get("k:2").isEnable());
        assertFalse("MapUtil.merge fail", target.get("k:1").isEnable());
    }

    @Test
    @SuppressWarnings({"unchecked"})
    public void merge_recursively() {
        Map<String, Object> base = createTree(1, 4, false);
        Map<String, Object> status = createTree(1, 4, true);

        MapUtil.copy(status, base, true);
        assertFalse("MapUtil.merge failed", (Boolean) base.get("enable"));

        Map<String, Object> children = (Map<String, Object>) base.get("children");
        assertFalse("MapUtil.merge failed", (Boolean) children.get("enable"));
    }


    private Map<String, Object> createTree(int no, int max, boolean isStatus) {
        Map<String, Object> tree = new HashMap<>();

        if (isStatus) {
            tree.put("enable", false);
        } else {
            tree.put("enable", true);
            tree.put("name", "name:" + no);
            tree.put("age", no);
        }

        if (no < max) {
            tree.put("children", createTree(++no, max, isStatus));
        }

        return tree;
    }


    @Test
    public void mergeNotFilterNulls() {
        Map<String, Channel> source = createSwitchMap(9);
        source.put("k:9", null);

        Map<String, Channel> target = createChannelMap(10);

        MapUtil.copy(source, target);
        assertNotNull("MapUtil.merge fail", target);
        assertNull("MapUtil.merge fail", source.get("k:9"));
        assertNull("MapUtil.merge fail", target.get("k:9"));
        assertTrue("MapUtil.merge fail", target.get("k:2").isEnable());
        assertFalse("MapUtil.merge fail", target.get("k:1").isEnable());
    }

    @Test
    public void mergeFilterNulls() {
        Map<String, Channel> source = createSwitchMap(9);
        source.put("k:9", null);

        Map<String, Channel> target = createChannelMap(10);

        MapUtil.copy(source, target, true);
        assertNotNull("MapUtil.merge fail", target);
        assertNull("MapUtil.merge fail", source.get("k:9"));
        assertNotNull("MapUtil.merge fail", target.get("k:9"));
        assertTrue("MapUtil.merge fail", target.get("k:2").isEnable());
        assertFalse("MapUtil.merge fail", target.get("k:1").isEnable());
        assertTrue("MapUtil.merge fail", target.get("k:9").isEnable());
    }

    private Map<String, Channel> createChannelMap(int num) {
        Map<String, Channel> map = new HashMap<>();

        String k; Channel v;
        for (int i = 0; i < num; i++) {
            k = "k:" + i;

            v = new Channel();
            v.setEnable(true);
            v.setName(k);

            map.put(k, v);
        }

        return map;
    }

    private Map<String, Channel> createSwitchMap(int num) {
        Map<String, Channel> map = new HashMap<>();

        String k; Channel v;
        for (int i = 0; i < num; i++) {
            k = "k:" + i;

            v = new Channel();
            v.setEnable(i % 2 == 0);

            map.put(k, v);
        }

        return map;
    }

    @Data
    static class Channel {
        private boolean enable;
        private String name;
    }
}