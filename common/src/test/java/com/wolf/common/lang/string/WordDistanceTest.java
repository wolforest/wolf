package com.wolf.common.lang.string;

import com.wolf.common.ds.string.WordDistance;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * com.wolf.common.lang.string
 *
 * @author Wingle
 * @since 2021/9/16 下午5:09
 **/
public class WordDistanceTest {

    @Test
    public void calculate_delete() {
        String first = "mouse";
        String second = "mouuse";
        int distance = WordDistance.get(first, second);

        assertEquals("WordDistance calculate fail",1, distance);
    }

    @Test
    public void calculate_1() {
        String first = "add";
        String second = "add1";
        int distance = WordDistance.get(first, second);

        assertEquals("WordDistance calculate fail",1, distance);
    }

    @Test
    public void calculate_2() {
        String first = "add";
        String second = "acd";
        int distance = WordDistance.get(first, second);

        assertEquals("WordDistance calculate fail",1, distance);
    }

    @Test
    public void calculate_3() {
        String first = "add";
        String second = "add";
        int distance = WordDistance.get(first, second);

        assertEquals("WordDistance calculate fail",0, distance);
    }

    @Test
    public void calculate_4() {
        String first = "add";
        String second = "acc";
        int distance = WordDistance.get(first, second);

        assertEquals("WordDistance calculate fail",2, distance);
    }

    @Test
    public void calculate_5() {
        String first = "ad";
        String second = "ac";
        int distance = WordDistance.get(first, second);

        assertEquals("WordDistance calculate fail",1, distance);
    }

}
