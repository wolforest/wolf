package cn.coderule.common.util.collection;

import cn.coderule.common.util.lang.collection.SetUtil;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

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

        assertEquals("1", SetUtil.first(stringSet), "SetUtil.first fail");
    }

    @Test
    public void last() {
        Set<String> stringSet = createSet();

        assertEquals("5", SetUtil.last(stringSet), "SetUtil.last fail");
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
