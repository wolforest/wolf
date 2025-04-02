package com.wolf.common.util.lang;

import lombok.Data;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/2/14 5:47 下午
 **/
public class BeanUtilTest {

    @Test
    @SuppressWarnings("all")
    public void testEquals() {
        assertTrue("ObjectUtil.equals fail", BeanUtil.equals(Integer.valueOf(1), 1));
        assertTrue("ObjectUtil.equals fail", BeanUtil.equals(Long.valueOf(1), 1L));
        assertTrue("ObjectUtil.equals fail", BeanUtil.equals(Double.valueOf(1.0), 1.0));
    }

    @Test
    public void testEquals1() {
    }

    @Test
    public void inArray() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void copyProperties() {
        Obj o1 = new Obj();
        o1.setName("o1");

        Obj o2 = new Obj();
        BeanUtil.copyPropertiesBak(o1, o2);

        assertEquals("BeanUtil.copyProperties fail", o1.getName(), o2.getName());
        assertEquals("BeanUtil.copyProperties fail", o1.getAge(), o2.getAge());
        assertNull("BeanUtil.copyProperties fail", o2.getAge());

        Obj o3 = new Obj();
        o3.setAge(10);
        BeanUtil.copyPropertiesBak(o1, o3);

        assertEquals("BeanUtil.copyProperties fail", o1.getName(), o3.getName());
        assertEquals("BeanUtil.copyProperties fail", o1.getAge(), o3.getAge());
        assertNull("BeanUtil.copyProperties fail", o3.getAge());

        Obj o4 = new Obj();
        o4.setAge(10);
        BeanUtil.copyPropertiesBak(o1, o4, true);

        assertEquals("BeanUtil.copyProperties fail", o1.getName(), o4.getName());
        assertNotEquals("BeanUtil.copyProperties fail", o1.getAge(), o4.getAge());
        assertNotNull("BeanUtil.copyProperties fail", o4.getAge());
        assertEquals("BeanUtil.copyProperties fail", Integer.valueOf(10), o4.getAge());
    }

    @Test
    public void testCopyProperties() {
    }

    @Test
    public void toMap() {
    }

    @Test
    public void toBean() {
    }

    @Test
    public void testEquals2() {
    }

    @Test
    public void newInstance() {
    }

    @Test
    public void isSimpleObject() {
    }

    @Test
    public void nullToBlank() {
    }

    @Test
    public void trim() {
    }

    @Test
    public void testToMap() {
        Map<String, Object> map = BeanUtil.toMap(DemoConst.class);

        assertEquals("BeanUtil.toMap failed", "G_PAY", map.get("GROUP_PAY"));
        assertEquals("BeanUtil.toMap failed", "PAYMENT", map.get("TOPIC_PAYMENT"));
    }

    @Test
    public void testToString() {
    }

    static class DemoConst {
        public static final String GROUP_PAY = "G_PAY";

        public static final String TOPIC_PAYMENT = "PAYMENT";
    }

    @Data
    static class Obj {
        private String name;
        private Integer age;
    }
}
