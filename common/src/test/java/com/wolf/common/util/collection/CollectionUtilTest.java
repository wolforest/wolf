package com.wolf.common.util.collection;

import lombok.Data;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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
        assertEquals("get wrong keys length", 6, ids.size());
        assertEquals("get wrong keys length", new Integer(2), ids.get(2));
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

        assertEquals("wrong map length", 6, map.size());
        assertEquals("map setting fail", "s:2", map.get(2).getName());
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
        assertEquals("sum fail", 3, result);
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

        assertEquals("group sum fail", 6, groupSum.get(0).longValue());
        assertEquals("group sum fail", 9, groupSum.get(1).longValue());
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
        assertEquals("group fail", 3, groups.get(1).size());
        assertEquals("group fail", 2, groups.get(0).size());
        assertEquals("group fail", 0, groups.get(0).get(0).getGender());
        assertEquals("group fail", 0, groups.get(0).get(1).getGender());
        assertEquals("group fail", 1, groups.get(1).get(0).getGender());
        assertEquals("group fail", 1, groups.get(1).get(1).getGender());
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
        assertEquals("join map fail", "name_2", p1.get(2).getName());
        assertNull("join map fail", p1.get(2).getP());

        CollectionUtil.join(p1, Person::getId, Person::setP, p2, Person::getId);
        assertNotNull("join two collection fail", p1.get(2).getP());
        assertEquals("join two collection fail", "s2_2", p1.get(2).getP().getName());
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
        assertEquals("convert fail", 10, bList.size());
        assertEquals("convert fail", "name:1", bList.get(1).getName());
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

        assertEquals("convert fail", 10, bList.size());
        assertEquals("convert fail", "name:1", bList.get(1).getName());
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