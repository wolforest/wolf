package cn.coderule.common.util.collection;

import cn.coderule.common.util.lang.collection.CollectionUtil;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2020/1/14 8:47 下午
 **/
public class CollectionUtilTest {

    @Test
    public void test_isType() {
        Collection<String> c = new ArrayList<>();
        c.add("abc");

        System.out.println(Arrays.toString(c.getClass().getTypeParameters()));
    }

    @Test
    public void keys() {
        List<Person> p1 = new ArrayList<>();

        Person t1;
        for (int i = 0; i < 6; i++) {
            t1 = new Person();
            t1.setId(i);

            p1.add(t1);
        }

        List<Integer> ids = CollectionUtil.keys(p1, Person::getId);
        assertEquals(6, ids.size(), "get wrong keys length");
        assertEquals(Integer.valueOf(2), ids.get(2), "get wrong keys length");
    }

    @Test
    public void toMap() {
        List<Person> p1 = new ArrayList<>();

        Person t1;
        for (int i = 0; i < 6; i++) {
            t1 = new Person();
            t1.setId(i);
            t1.setName("s:" + i);

            if (i % 2 == 0) {
                t1.setGender(0);
            } else {
                t1.setGender(1);
            }
            p1.add(t1);
        }

        Map<Integer, Person> map = CollectionUtil.map(p1, Person::getId);

        assertEquals(6, map.size(), "wrong map length");
        assertEquals("s:2", map.get(2).getName(), "map setting fail");
    }

    @Test
    public void sum_long_work() {
        List<Person> p1 = new ArrayList<>();

        Person t1;
        for (int i = 1; i < 3; i++) {
            t1 = new Person();
            t1.setId(i);
            t1.setName("s:" + i);

            if (i % 2 == 0) {
                t1.setGender(0);
            } else {
                t1.setGender(1);
            }
            p1.add(t1);
        }

        long result = CollectionUtil.sum(p1, Person::getId);
        assertEquals(3, result, "sum fail");
    }

    @Test
    public void groupAndSum() {
        List<Person> p1 = new ArrayList<>();

        Person t1;
        for (int i = 1; i < 6; i++) {
            t1 = new Person();
            t1.setId(i);

            if (i % 2 == 0) {
                t1.setGender(0);
            } else {
                t1.setGender(1);
            }

            p1.add(t1);
        }
        Map<Integer, Long> groupSum = CollectionUtil.groupAndSum(p1, Person::getGender, Person::getId);
        Map<Integer, List<Person>> groups = CollectionUtil.group(p1, Person::getGender);

        assertEquals(6, groupSum.get(0).longValue(), "group sum fail");
        assertEquals(9, groupSum.get(1).longValue(), "group sum fail");
    }

    @Test
    public void group() {
        List<Person> p1 = new ArrayList<>();

        Person t1;
        for (int i = 1; i < 6; i++) {
            t1 = new Person();
            t1.setId(i);

            if (i % 2 == 0) {
                t1.setGender(0);
            } else {
                t1.setGender(1);
            }

            p1.add(t1);
        }

        Map<Integer, List<Person>> groups = CollectionUtil.group(p1, Person::getGender);
        assertEquals(3, groups.get(1).size(), "group fail");
        assertEquals(2, groups.get(0).size(), "group fail");
        assertEquals(0, groups.get(0).get(0).getGender(), "group fail");
        assertEquals(0, groups.get(0).get(1).getGender(), "group fail");
        assertEquals(1, groups.get(1).get(0).getGender(), "group fail");
        assertEquals(1, groups.get(1).get(1).getGender(), "group fail");
    }

    @Test
    public void join() {
        List<Person> p1 = new ArrayList<>();
        List<Person> p2 = new ArrayList<>();
        Map<Integer, String> nameMap = new HashMap<>();

        Person t1, t2;
        for (int i = 0; i < 6; i++) {
            t1 = new Person();
            t1.setId(i);

            t2 = new Person();
            t2.setId(i);
            t2.setName("s2_" + i);

            if (i % 2 == 0) {
                t1.setGender(0);
                t2.setGender(0);
            } else {
                t1.setGender(1);
                t2.setGender(1);
            }

            p1.add(t1);
            p2.add(t2);
            nameMap.put(i, "name_" + i);
        }

        CollectionUtil.join(p1, Person::getId, Person::setName, nameMap);
        assertEquals("name_2", p1.get(2).getName(), "join map fail");
        assertNull(p1.get(2).getP(), "join map fail");

        CollectionUtil.join(p1, Person::getId, Person::setP, p2, Person::getId);
        assertNotNull(p1.get(2).getP(), "join two collection fail");
        assertEquals("s2_2", p1.get(2).getP().getName(), "join two collection fail");
    }

    @Test
    public void static_to_method_work() {
        List<A> aList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            A a = new A();
            a.setName("name:" + i);

            aList.add(a);
        }

        List<B> bList = CollectionUtil.to(aList, Convert::convert);
        assertEquals(10, bList.size(), "convert fail");
        assertEquals("name:1", bList.get(1).getName(), "convert fail");
    }

    @Test
    public void instance_method_to_work() {
        List<A> aList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            A a = new A();
            a.setName("name:" + i);

            aList.add(a);
        }

        Converter converter = new Converter();
        List<B> bList = CollectionUtil.to(aList, converter::convert);

        assertEquals(10, bList.size(), "convert fail");
        assertEquals("name:1", bList.get(1).getName(), "convert fail");
    }

    @Test
    public void checkNotEmpty() {
    }

    @Test
    public void empty() {
    }

    @Test
    public void sub() {
    }

    @Test
    public void notEmpty() {
    }

    @Test
    public void isEmpty() {
    }

    @Test
    public void first() {
    }

    @Test
    public void firstNotNull() {
    }

    @Test
    public void last() {
    }

    @Test
    public void add() {
    }

    @Test
    public void filter() {
    }

    @Test
    public void filterFirst() {
    }

    @Test
    public void filterAny() {
    }

    @Test
    public void map() {
    }

    @Test
    public void testMap() {
    }

    @Test
    public void testMap1() {
    }

    @Test
    public void sum() {
    }

    @Test
    public void to() {
    }

    @Test
    public void merge() {
    }

    @Test
    public void isSub_integer_list_1() {
        List<Integer> parent = List.of(1, 2, 3, 4, 5);
        List<Integer> child = List.of(1, 2, 3);

        assertTrue(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_integer_list_2() {
        List<Integer> parent = List.of(1, 2, 3, 4, 5);
        List<Integer> child = List.of(1, 6, 3);

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_integer_list_3() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = List.of(1, 6, 3);

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_null_parent() {
        List<Integer> parent = null;
        List<Integer> child = List.of(1, 2, 3);

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_null_child_1() {
        List<Integer> parent = null;
        List<Integer> child = null;

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_null_child_2() {
        List<Integer> parent = List.of();
        List<Integer> child = null;

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_null_child_3() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = null;

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_empty_child_1() {
        List<Integer> parent = null;
        List<Integer> child = List.of();

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_empty_child_2() {
        List<Integer> parent = List.of();
        List<Integer> child = List.of();

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_empty_child_3() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = List.of();

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }

    @Test
    public void isSub_empty_parent() {
        List<Integer> parent = List.of();
        List<Integer> child = List.of(1, 2, 3);

        assertFalse(CollectionUtil.isSub(parent, child), "CollectionUtil.isSub failed");
    }


    @Test
    public void isDifferent_integer_list_1() {
        List<Integer> parent = List.of(1, 2, 3, 4, 5);
        List<Integer> child = List.of(1, 2, 3);

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_integer_list_2() {
        List<Integer> parent = List.of(1, 2, 3, 4, 5);
        List<Integer> child = List.of(1, 6, 3);

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_integer_list_3() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = List.of(1, 6, 3);

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_integer_list_4() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = List.of(1, 2, 3);

        assertFalse(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_null_parent() {
        List<Integer> parent = null;
        List<Integer> child = List.of(1, 2, 3);

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_null_child_1() {
        List<Integer> parent = null;
        List<Integer> child = null;

        assertFalse(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_null_child_2() {
        List<Integer> parent = List.of();
        List<Integer> child = null;

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_null_child_3() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = null;

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_empty_child_1() {
        List<Integer> parent = null;
        List<Integer> child = List.of();

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_empty_child_2() {
        List<Integer> parent = List.of();
        List<Integer> child = List.of();

        assertFalse(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_empty_child_3() {
        List<Integer> parent = List.of(1, 2, 3);
        List<Integer> child = List.of();

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }

    @Test
    public void isDifferent_empty_parent() {
        List<Integer> parent = List.of();
        List<Integer> child = List.of(1, 2, 3);

        assertTrue(CollectionUtil.isDifferent(parent, child), "CollectionUtil.isDifferent failed");
    }




    @Data
    static class A {
        private String name;
    }

    @Data
    static class B {
        private String name;
    }

    static class Convert {
        public static B convert(A a) {
            B b = new B();
            b.setName(a.getName());

            return b;
        }
    }

    static class Converter {
        public B convert(A a) {
            B b = new B();
            b.setName(a.getName());

            return b;
        }
    }

    @Data
    static class Person {
        private int id;
        private String name;
        private int Gender;

        private Person p;
    }
}
