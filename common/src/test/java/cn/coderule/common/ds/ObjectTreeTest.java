package cn.coderule.common.ds;

import cn.coderule.common.ds.ObjectTree;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectTreeTest {

    public void test_root_work() {
        ObjectTree tree = ObjectTree.newInstance()
                .put("l1-1", "a")
                .put("l1-2", "b")
                .put("l1-3", "c");


    }

    public void test_first_level_work() {

    }

    public void test_second_level_work() {

    }

    @Test
    public void test_four_level_work() {
        //@formatter:off
        ObjectTree tree = ObjectTree.newInstance()
                .put("l1-1", "a")
                .put("l1-2", "b")
                .put("l1-3", "c")
                    .children("l1-4")
                    .put("l2-1", "d")
                    .put("l2-2", "e")
                    .put("l2-3", "f")
                        .children("l2-4")
                        .put("l3-1", "h")
                        .put("l3-2", "i")
                        .put("l3-3", "j")
                .root()
                .put("l1-5", "a")
                .put("l1-6", "b")
                .put("l1-7", "c")
                            .parent("l1-8", "l2-8", "l3-8")
                            .put("l4-5", "a")
                            .put("l4-6", "b")
                            .put("l4-7", "c");
        //@formatter:on

        assertEquals("ObjectTree build fail", "a", tree.getObject("l1-1", String.class));
        assertEquals("ObjectTree build fail", "b", tree.getObject(String.class, "l1-8", "l2-8", "l3-8", "l4-6"));

    }


}
