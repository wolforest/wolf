package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.string.StringUtil;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/12/10 9:57 上午
 **/
public class StringUtilTest {

    @Test
    public void hasValue() {
        assertTrue(StringUtil.notEmpty(" "), "space hasValue");
        assertFalse(StringUtil.notEmpty(null), "null do not hasValue");
        assertFalse(StringUtil.notEmpty(""), "empty string do not hasValue");
        assertTrue(StringUtil.notEmpty("abc"), "common string hasValue");
    }

    @Test
    public void joinWith() {
        String s;

        s = StringUtil.joinWith(" ", "f", "", "t");
        assertEquals(s, "f  t", "StringUtil.join fail");

        s = StringUtil.joinSkipBlank(" ", "f", "", "t");
        assertEquals(s, "f t", "StringUtil.join fail");
    }

    @Test
    public void isTrue() {
    }

    @Test
    public void isFalse() {
    }

    @Test
    public void containsSpace() {
    }

    @Test
    public void ltrim() {
        String a = "abcdef";
        String prefix = "abc";

        a = StringUtil.ltrim(a, prefix);
        String expected = "def";

        assertEquals(expected, a, "StringUtil.ltrim fail");
    }

    @Test
    public void rtrim() {
        String a = "abcdef";
        String suffix = "def";
        String expected = "abc";

        a = StringUtil.rtrim(a, suffix);
        assertEquals(expected, a, "StringUtil.rtrim fail");

    }

    @Test
    public void rtrim_long_string() {
        String a = "abcdefghijklmn";
        String suffix = "lmn";
        String expected = "abcdefghijk";

        a = StringUtil.rtrim(a, suffix);
        assertEquals(expected, a, "StringUtil.rtrim fail");

    }

    @Test
    public void lowerCamel() {
        String s = "trade_order_name";
        String expect = "tradeOrderName";

        assertEquals(expect, StringUtil.lowerCamel(s), "StringUtil.lowerCamel fail");
        assertEquals(expect, StringUtil.camel(s), "StringUtil.lowerCamel fail");

        assertEquals(s, StringUtil.lowerUnderscore(expect), "StringUtil.lowerUnderscore fail");
        assertEquals(s, StringUtil.lowerUnderscore(expect), "StringUtil.lowerUnderscore fail");
    }

    @Test
    public void lowerCamel_dot() {
        String s1 = "trade.order.name";
        String s2 = "trade_order_name";
        String expect = "tradeOrderName";

        assertEquals(expect, StringUtil.lowerCamel(s1, "."), "StringUtil.lowerCamel fail");
        assertEquals(expect, StringUtil.camel(s1, "."), "StringUtil.lowerCamel fail");

        assertEquals(s2, StringUtil.lowerUnderscore(expect), "StringUtil.lowerUnderscore fail");
        assertEquals(s2, StringUtil.lowerUnderscore(expect), "StringUtil.lowerUnderscore fail");

        assertEquals(s1, StringUtil.camelTo(expect, "."), "StringUtil.lowerUnderscore fail");
        assertEquals(s1, StringUtil.camelTo(expect, "."), "StringUtil.lowerUnderscore fail");
    }

    @Test
    public void lowerCamel_minus() {
        String s1 = "trade-order-name";
        String s2 = "trade_order_name";
        String expect = "tradeOrderName";

        assertEquals(expect, StringUtil.lowerCamel(s1, "-"), "StringUtil.lowerCamel fail");
        assertEquals(expect, StringUtil.camel(s1, "-"), "StringUtil.lowerCamel fail");

        assertEquals(s2, StringUtil.lowerUnderscore(expect), "StringUtil.lowerUnderscore fail");
        assertEquals(s2, StringUtil.lowerUnderscore(expect), "StringUtil.lowerUnderscore fail");


        assertEquals(s1, StringUtil.camelTo(expect, "-"), "StringUtil.lowerUnderscore fail");
        assertEquals(s1, StringUtil.camelTo(expect, "-"), "StringUtil.lowerUnderscore fail");
    }

    @Test
    public void capitalize() {
        assertNull(StringUtil.capitalize(null), "capitalize fail");
        assertEquals("", StringUtil.capitalize(""), "capitalize fail");
        assertEquals(" ", StringUtil.capitalize(" "), "capitalize fail");

        assertEquals("Trade", StringUtil.capitalize("trade"), "capitalize fail");
        assertEquals("Trade", StringUtil.capitalize("Trade"), "capitalize fail");
        assertEquals("TRADE", StringUtil.capitalize("tRADE"), "capitalize fail");
    }

    @Test
    public void uncapitalize() {
        assertNull(StringUtil.uncapitalize(null), "capitalize fail");
        assertEquals("", StringUtil.uncapitalize(""), "capitalize fail");
        assertEquals(" ", StringUtil.uncapitalize(" "), "capitalize fail");

        assertEquals("trade", StringUtil.uncapitalize("trade"), "capitalize fail");
        assertEquals("trade", StringUtil.uncapitalize("Trade"), "capitalize fail");
        assertEquals("tRADE", StringUtil.uncapitalize("TRADE"), "capitalize fail");
    }

    @Test
    public void ucWords() {
        assertNull(StringUtil.ucWords(null), "capitalize fail");
        assertEquals("", StringUtil.ucWords(""), "capitalize fail");
        assertEquals(" ", StringUtil.ucWords(" "), "capitalize fail");

        assertEquals("Trade.Order.Line", StringUtil.ucWords("trade.order.line", "."), "capitalize fail");
        assertEquals("Trade.Order.Line", StringUtil.ucWords("Trade.Order.Line", "."), "capitalize fail");
        assertEquals("TRAde.Order.Line", StringUtil.ucWords("TRAde.Order.Line", "."), "capitalize fail");
    }

    @Test
    public void lcWords() {
        assertNull(StringUtil.lcWords(null), "capitalize fail");
        assertEquals("", StringUtil.lcWords(""), "capitalize fail");
        assertEquals(" ", StringUtil.lcWords(" "), "capitalize fail");

        assertEquals("trade.order.line", StringUtil.lcWords("Trade.Order.Line", "."), "capitalize fail");
        assertEquals("trade.order.line", StringUtil.lcWords("Trade.Order.Line", "."), "capitalize fail");
        assertEquals("tRAde.order.line", StringUtil.lcWords("TRAde.Order.Line", "."), "capitalize fail");
    }

    @Test
    public void split() {
        String s;

        s = "hello";
        String[] sArr1 = StringUtil.split(s, "-");
        assertEquals(1, sArr1.length, "StringUtil.split fail");
        assertEquals(s, sArr1[0], "StringUtil.split fail");

        s = "hello-world";
        String[] sArr2 = StringUtil.split(s, "-");
        assertEquals(2, sArr2.length, "StringUtil.split fail");
        assertEquals("hello", sArr2[0], "StringUtil.split fail");
        assertEquals("world", sArr2[1], "StringUtil.split fail");

        s = "h/e/l/l/o";
        String[] sArr3 = s.split("/", 2);
        assertEquals(2, sArr3.length, "StringUtil.split fail");
        assertEquals("h", sArr3[0], "StringUtil.split fail");
        assertEquals("e/l/l/o", sArr3[1], "StringUtil.split fail");

        s = "hello";
        String[] sArr4 = s.split("/", 2);
        assertEquals(1, sArr4.length, "StringUtil.split fail");
        assertEquals("hello", sArr4[0], "StringUtil.split fail");
    }

    @Test
    public void remove() {
        List<String> subs = Arrays.asList(" dLocal", " dokypay");
        String s = "I love dlocal DLocal dokypay Dokypay";
        String expected = "I love";

        assertEquals(expected, StringUtil.remove(s, subs), "StringUtil.remove fail");
    }

    @Test
    public void remove1() {
        List<String> subs = Arrays.asList("dLocal", "dokypay");
        String s = "I love dlocal DLocal dokypay Dokypay";
        String expected = "I love    ";

        assertEquals(expected, StringUtil.remove(s, subs), "StringUtil.remove fail");
    }

    @Test
    public void replace() {
        Map<String, String> map = new HashMap<>();
        map.put("dlocal", "dl");
        map.put("dokypay", "dk");
        map.put("PayTm", "tm");

        String s = "I love dlocal DLocal dokypay Dokypay";
        String expected = "I love dl dl dk dk";
        assertEquals(expected, StringUtil.replace(s, map), "StringUtil.replace fail");
    }

    @Test
    public void substringLastTo() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals(expected, StringUtil.substringLastTo(s, sep), "StringUtil.substringTo fail");

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals(expected, StringUtil.substringLastTo(s, sep, true), "StringUtil.substringTo fail");

        s = "abc:d";
        sep = ":";
        expected = "abc";
        assertEquals(expected, StringUtil.substringLastTo(s, sep), "StringUtil.substringTo fail");

        s = "abc:d:e";
        sep = ":";
        expected = "abc:d";
        assertEquals(expected, StringUtil.substringLastTo(s, sep), "StringUtil.substringTo fail");
    }

    @Test
    public void substringLastFrom() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals(expected, StringUtil.substringLastFrom(s, sep), "StringUtil.substringFrom fail");

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals(expected, StringUtil.substringLastFrom(s, sep, true), "StringUtil.substringFrom fail");

        s = "abc:d";
        sep = ":";
        expected = "d";
        assertEquals(expected, StringUtil.substringLastFrom(s, sep), "StringUtil.substringFrom fail");

        s = "abc:d:e";
        sep = ":";
        expected = "e";
        assertEquals(expected, StringUtil.substringLastFrom(s, sep), "StringUtil.substringFrom fail");
    }

    @Test
    public void substringTo() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals(expected, StringUtil.substringTo(s, sep), "StringUtil.substringTo fail");

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals(expected, StringUtil.substringTo(s, sep, true), "StringUtil.substringTo fail");

        s = "abc:d";
        sep = ":";
        expected = "abc";
        assertEquals(expected, StringUtil.substringTo(s, sep), "StringUtil.substringTo fail");
    }

    @Test
    public void substringFrom() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals(expected, StringUtil.substringFrom(s, sep), "StringUtil.substringFrom fail");

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals(expected, StringUtil.substringFrom(s, sep, true), "StringUtil.substringFrom fail");

        s = "abc:d";
        sep = ":";
        expected = "d";
        assertEquals(expected, StringUtil.substringFrom(s, sep), "StringUtil.substringFrom fail");

        //分隔符为2个字符的场景
        s="jdbc:mysql://127.0.0.1:3306";
        sep="//";
        expected = "127.0.0.1:3306";
        assertEquals(expected, StringUtil.substringFrom(s, sep), "StringUtil.substringFrom fail");

    }


    @Test
    public void substringToBlank() {
        String s = "Failed to invoke the method payout in the service com.wolf.business.pay.api.service.gateway.PayoutGatewayService. Tried 1 times of the providers [172.16.192.15:9080] (1/1) from the registry localhost:9090 on the consumer 47.111.7.95 using the dubbo version 2.7.3. Last error is: Invoke remote method timeout. method: payout, provider:";
        String expected = "Failed to invoke the method";

        String r2 = StringUtil.substringToBlank(s, 20);
        assertNotEquals(expected, r2, "StringUtil.substringToBlank fail");

        String r1 = StringUtil.substringToBlank(s, 22);
        assertEquals(expected, r1, "StringUtil.substringToBlank fail");

        String r3 = StringUtil.substringToBlank(s, 21);
        assertEquals(expected, r3, "StringUtil.substringToBlank fail");

        String r4 = StringUtil.substringToBlank(s, 23);
        assertEquals(expected, r4, "StringUtil.substringToBlank fail");
    }

    @Test
    public void startWith() {
        String s1 = " [";
        String s2 = " {";

        assertTrue(StringUtil.startsWith(s1, "[", true), "startWith fail");
        assertFalse(StringUtil.startsWith(s2, "[", true), "startWith fail");
    }

    @Test
    public void endsWith() {
        String s1 = "a_no ";
        String s2 = "b_id";

        assertTrue(StringUtil.endsWith(s1, "_no", true), "endsWith fail");
        assertTrue(StringUtil.endsWith(s2, "_id", true), "endsWith fail");
    }


    @Test
    public void isEmpty() {
    }

    @Test
    public void testIsEmpty() {
    }

    @Test
    public void notBlank() {
    }

    @Test
    public void isBlank() {
    }

    @Test
    public void testIsBlank() {
    }

    @Test
    public void testNotBlank() {
    }

    @Test
    public void notEmpty() {
    }

    @Test
    public void isZero() {
        assertFalse(StringUtil.isZero(null), "StringUtil.isZero failed");
        assertFalse(StringUtil.isZero(""), "StringUtil.isZero failed");

        assertTrue(StringUtil.isZero("0"), "StringUtil.isZero failed");
        assertTrue(StringUtil.isZero("000"), "StringUtil.isZero failed");
        assertTrue(StringUtil.isZero("00000000"), "StringUtil.isZero failed");

        assertFalse(StringUtil.isZero("0101"), "StringUtil.isZero failed");
        assertFalse(StringUtil.isZero("101"), "StringUtil.isZero failed");
        assertFalse(StringUtil.isZero("abc"), "StringUtil.isZero failed");
    }

    @Test
    public void testIsZero() {
    }

    @Test
    public void join() {
    }

    @Test
    public void joinSkipBlank() {
    }

    @Test
    public void quote() {
    }

    @Test
    public void testQuote() {
    }

    @Test
    public void camel() {
    }

    @Test
    public void testCamel() {
    }

    @Test
    public void upperCamel() {
    }

    @Test
    public void testUpperCamel() {
    }

    @Test
    public void camelTo() {
    }

    @Test
    public void testCamelTo() {
    }

    @Test
    public void underscore() {
    }

    @Test
    public void lowerUnderscore() {
    }

    @Test
    public void upperUnderscore() {
    }

    @Test
    public void addSlash() {
    }

    @Test
    public void uuid() {
    }

    @Test
    public void startsWith() {
    }

    @Test
    public void testStartsWith() {
    }

    @Test
    public void trim() {
    }

    @Test
    public void contains() {
        assertFalse(StringUtil.contains(null, "a"), "StringUtil.contains failed");
        assertFalse(StringUtil.contains("", "a"), "StringUtil.contains failed");
        assertFalse(StringUtil.contains(" ", "a"), "StringUtil.contains failed");

        assertTrue(StringUtil.contains("abc", "a"), "StringUtil.contains failed");
        assertTrue(StringUtil.contains("cabc", "ab"), "StringUtil.contains failed");

        assertFalse(StringUtil.contains("cabc", "ac"), "StringUtil.contains failed");
    }

    @Test
    public void leftPad() {
    }

    @Test
    public void rightPad() {
    }

    @Test
    public void format() {
    }

    @Test
    public void mask() {
    }

    @Test
    public void testMask() {
    }

    @Test
    public void validateEmail() {
    }

    @Test
    public void maskEmail() {
    }

    @Test
    public void escapeCsvSpecialCharacters() {
    }

    @Test
    public void repeat() {
    }
}
