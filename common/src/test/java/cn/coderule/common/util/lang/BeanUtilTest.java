package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.bean.BeanUtil;
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
