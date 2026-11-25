package cn.coderule.common.util.collection;

import cn.coderule.common.util.lang.collection.MapUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

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
        assertTrue(MapUtil.isEmpty(null), "null isEmpty failed");
        assertTrue(MapUtil.isEmpty(MapUtil.empty()), "emptyMap isEmpty failed");

        Map<String, Object> map = new HashMap<>();
        assertTrue(MapUtil.isEmpty(map), "emptyMap isEmpty failed");

        map.put(null, "nullValue");
        assertTrue(MapUtil.isEmpty(map, true), "null key isEmpty failed");

        map.put("nullKey", null);
        assertTrue(MapUtil.isEmpty(map, true), "null value isEmpty failed");

        map.put("a", "abc");
        assertFalse(MapUtil.isEmpty(map, true), "map notEmpty failed");
    }

    @Test
    public void containsNull() {
        Map<String, Object> map = new HashMap<>();
        assertFalse(MapUtil.containsNull(map), "containsNull failed");

        map.put("nullKey", null);
        assertTrue(MapUtil.containsNull(map), "containsNull failed");

        map.put(null, "nullValue");
        assertTrue(MapUtil.containsNull(map), "containsNull failed");
    }

    @Test
    public void containsNullValue() {
        Map<String, Object> map = new HashMap<>();
        map.put("nullKey", null);

        assertTrue(map.containsValue(null), "containsNullValue failed");
    }

    @Test
    public void containsNullKey() {
        Map<String, Object> map = new HashMap<>();
        map.put(null, "nullValue");

        assertTrue(map.containsKey(null), "containsNullKey failed");
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

        assertNotNull(sub, "subMap failed");
        assertEquals(1, sub.get("a"), "subMap failed");
        assertTrue(sub.containsKey("e"), "subMap failed");
        assertNull(sub.get("e"), "subMap failed");
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

        assertNotNull(sub, "subMap failed");
        assertTrue(sub.isEmpty(), "subMap failed");
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

        assertNotNull(sub, "subMap failed");
        assertEquals(1, sub.get("a"), "subMap failed");
        assertFalse(sub.containsKey("e"), "subMap skipNulls failed");
        assertNull(sub.get("e"), "subMap failed");
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

        assertNotNull(sub, "subMap failed");
        assertEquals(1, sub.get("a"), "subMap failed");
        assertTrue(sub.containsKey("e"), "subMap failed");
        assertNull(sub.get("e"), "subMap failed");
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


        assertNotNull(sub, "subMap failed");
        assertEquals(1, sub.get("a"), "subMap failed");
        assertFalse(sub.containsKey("e"), "subMap skipNulls failed");
        assertNull(sub.get("e"), "subMap failed");
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
        assertFalse(map.containsKey("a"), "MapUtil.remove failed");
        assertFalse(map.containsKey("b"), "MapUtil.remove failed");
        assertFalse(map.containsKey("e"), "MapUtil.remove failed");
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
        assertFalse(map.containsKey("a"), "MapUtil.remove failed");
        assertFalse(map.containsKey("b"), "MapUtil.remove failed");
        assertFalse(map.containsKey("e"), "MapUtil.remove failed");
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
        assertFalse(MapUtil.contains(map, key1), "MapUtil.contains failed");
        assertFalse(MapUtil.contains(map, keyList1), "MapUtil.contains failed");

        String[] key2 = new String[]{"a", "b"};
        List<String> keyList2 = Arrays.asList(key2);
        assertTrue(MapUtil.contains(map, key2), "MapUtil.contains failed");
        assertTrue(MapUtil.contains(map, keyList2), "MapUtil.contains failed");


        assertTrue(MapUtil.contains(map, "a", "b", "c"), "MapUtil.contains failed");
        assertFalse(MapUtil.contains(map, "a", "b", "f"), "MapUtil.contains failed");
        assertFalse(MapUtil.contains(map, "n", "h", "f"), "MapUtil.contains failed");
    }

    @Test
    public void merge() {
        Map<String, Channel> source = createSwitchMap(10);
        Map<String, Channel> target = createChannelMap(10);

        MapUtil.copy(source, target);
        assertNotNull(target, "MapUtil.merge fail");
        assertTrue(target.get("k:2").isEnable(), "MapUtil.merge fail");
        assertFalse(target.get("k:1").isEnable(), "MapUtil.merge fail");
    }

    @Test
    @SuppressWarnings({"unchecked"})
    public void merge_recursively() {
        Map<String, Object> base = createTree(1, 4, false);
        Map<String, Object> status = createTree(1, 4, true);

        MapUtil.copy(status, base, true);
        assertFalse((Boolean) base.get("enable"), "MapUtil.merge failed");

        Map<String, Object> children = (Map<String, Object>) base.get("children");
        assertFalse((Boolean) children.get("enable"), "MapUtil.merge failed");
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
        assertNotNull(target, "MapUtil.merge fail");
        assertNull(source.get("k:9"), "MapUtil.merge fail");
        assertNull(target.get("k:9"), "MapUtil.merge fail");
        assertTrue(target.get("k:2").isEnable(), "MapUtil.merge fail");
        assertFalse(target.get("k:1").isEnable(), "MapUtil.merge fail");
    }

    @Test
    public void mergeFilterNulls() {
        Map<String, Channel> source = createSwitchMap(9);
        source.put("k:9", null);

        Map<String, Channel> target = createChannelMap(10);

        MapUtil.copy(source, target, true);
        assertNotNull(target, "MapUtil.merge fail");
        assertNull(source.get("k:9"), "MapUtil.merge fail");
        assertNotNull(target.get("k:9"), "MapUtil.merge fail");
        assertTrue(target.get("k:2").isEnable(), "MapUtil.merge fail");
        assertFalse(target.get("k:1").isEnable(), "MapUtil.merge fail");
        assertTrue(target.get("k:9").isEnable(), "MapUtil.merge fail");
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
