package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.string.StringUtil;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * com.wolf.common.util
 *
 * @author Wingle
 * @since 2019/12/10 9:57 上午
 **/
public class StringUtilTest {

    @Test
    public void hasValue() {
        assertTrue("space hasValue", StringUtil.notEmpty(" "));
        assertFalse("null do not hasValue", StringUtil.notEmpty(null));
        assertFalse("empty string do not hasValue", StringUtil.notEmpty(""));
        assertTrue("common string hasValue", StringUtil.notEmpty("abc"));
    }

    @Test
    public void joinWith() {
        String s;

        s = StringUtil.joinWith(" ", "f", "", "t");
        assertEquals("StringUtil.join fail", s, "f  t");

        s = StringUtil.joinSkipBlank(" ", "f", "", "t");
        assertEquals("StringUtil.join fail", s, "f t");
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

        assertEquals("StringUtil.ltrim fail", expected, a);
    }

    @Test
    public void rtrim() {
        String a = "abcdef";
        String suffix = "def";
        String expected = "abc";

        a = StringUtil.rtrim(a, suffix);
        assertEquals("StringUtil.rtrim fail", expected, a);

    }

    @Test
    public void rtrim_long_string() {
        String a = "abcdefghijklmn";
        String suffix = "lmn";
        String expected = "abcdefghijk";

        a = StringUtil.rtrim(a, suffix);
        assertEquals("StringUtil.rtrim fail", expected, a);

    }

    @Test
    public void lowerCamel() {
        String s = "trade_order_name";
        String expect = "tradeOrderName";

        assertEquals("StringUtil.lowerCamel fail", expect, StringUtil.lowerCamel(s));
        assertEquals("StringUtil.lowerCamel fail", expect, StringUtil.camel(s));

        assertEquals("StringUtil.lowerUnderscore fail", s, StringUtil.lowerUnderscore(expect));
        assertEquals("StringUtil.lowerUnderscore fail", s, StringUtil.lowerUnderscore(expect));
    }

    @Test
    public void lowerCamel_dot() {
        String s1 = "trade.order.name";
        String s2 = "trade_order_name";
        String expect = "tradeOrderName";

        assertEquals("StringUtil.lowerCamel fail", expect, StringUtil.lowerCamel(s1, "."));
        assertEquals("StringUtil.lowerCamel fail", expect, StringUtil.camel(s1, "."));

        assertEquals("StringUtil.lowerUnderscore fail", s2, StringUtil.lowerUnderscore(expect));
        assertEquals("StringUtil.lowerUnderscore fail", s2, StringUtil.lowerUnderscore(expect));

        assertEquals("StringUtil.lowerUnderscore fail", s1, StringUtil.camelTo(expect, "."));
        assertEquals("StringUtil.lowerUnderscore fail", s1, StringUtil.camelTo(expect, "."));
    }

    @Test
    public void lowerCamel_minus() {
        String s1 = "trade-order-name";
        String s2 = "trade_order_name";
        String expect = "tradeOrderName";

        assertEquals("StringUtil.lowerCamel fail", expect, StringUtil.lowerCamel(s1, "-"));
        assertEquals("StringUtil.lowerCamel fail", expect, StringUtil.camel(s1, "-"));

        assertEquals("StringUtil.lowerUnderscore fail", s2, StringUtil.lowerUnderscore(expect));
        assertEquals("StringUtil.lowerUnderscore fail", s2, StringUtil.lowerUnderscore(expect));


        assertEquals("StringUtil.lowerUnderscore fail", s1, StringUtil.camelTo(expect, "-"));
        assertEquals("StringUtil.lowerUnderscore fail", s1, StringUtil.camelTo(expect, "-"));
    }

    @Test
    public void capitalize() {
        assertNull("capitalize fail", StringUtil.capitalize(null));
        assertEquals("capitalize fail", "", StringUtil.capitalize(""));
        assertEquals("capitalize fail", " ", StringUtil.capitalize(" "));

        assertEquals("capitalize fail", "Trade", StringUtil.capitalize("trade"));
        assertEquals("capitalize fail", "Trade", StringUtil.capitalize("Trade"));
        assertEquals("capitalize fail", "TRADE", StringUtil.capitalize("tRADE"));
    }

    @Test
    public void uncapitalize() {
        assertNull("capitalize fail", StringUtil.uncapitalize(null));
        assertEquals("capitalize fail", "", StringUtil.uncapitalize(""));
        assertEquals("capitalize fail", " ", StringUtil.uncapitalize(" "));

        assertEquals("capitalize fail", "trade", StringUtil.uncapitalize("trade"));
        assertEquals("capitalize fail", "trade", StringUtil.uncapitalize("Trade"));
        assertEquals("capitalize fail", "tRADE", StringUtil.uncapitalize("TRADE"));
    }

    @Test
    public void ucWords() {
        assertNull("capitalize fail", StringUtil.ucWords(null));
        assertEquals("capitalize fail", "", StringUtil.ucWords(""));
        assertEquals("capitalize fail", " ", StringUtil.ucWords(" "));

        assertEquals("capitalize fail", "Trade.Order.Line", StringUtil.ucWords("trade.order.line", "."));
        assertEquals("capitalize fail", "Trade.Order.Line", StringUtil.ucWords("Trade.Order.Line", "."));
        assertEquals("capitalize fail", "TRAde.Order.Line", StringUtil.ucWords("TRAde.Order.Line", "."));
    }

    @Test
    public void lcWords() {
        assertNull("capitalize fail", StringUtil.lcWords(null));
        assertEquals("capitalize fail", "", StringUtil.lcWords(""));
        assertEquals("capitalize fail", " ", StringUtil.lcWords(" "));

        assertEquals("capitalize fail", "trade.order.line", StringUtil.lcWords("Trade.Order.Line", "."));
        assertEquals("capitalize fail", "trade.order.line", StringUtil.lcWords("Trade.Order.Line", "."));
        assertEquals("capitalize fail", "tRAde.order.line", StringUtil.lcWords("TRAde.Order.Line", "."));
    }

    @Test
    public void split() {
        String s;

        s = "hello";
        String[] sArr1 = StringUtil.split(s, "-");
        assertEquals("StringUtil.split fail", 1, sArr1.length);
        assertEquals("StringUtil.split fail", s, sArr1[0]);

        s = "hello-world";
        String[] sArr2 = StringUtil.split(s, "-");
        assertEquals("StringUtil.split fail", 2, sArr2.length);
        assertEquals("StringUtil.split fail", "hello", sArr2[0]);
        assertEquals("StringUtil.split fail", "world", sArr2[1]);

        s = "h/e/l/l/o";
        String[] sArr3 = s.split("/", 2);
        assertEquals("StringUtil.split fail", 2, sArr3.length);
        assertEquals("StringUtil.split fail", "h", sArr3[0]);
        assertEquals("StringUtil.split fail", "e/l/l/o", sArr3[1]);

        s = "hello";
        String[] sArr4 = s.split("/", 2);
        assertEquals("StringUtil.split fail", 1, sArr4.length);
        assertEquals("StringUtil.split fail", "hello", sArr4[0]);
    }

    @Test
    public void remove() {
        List<String> subs = Arrays.asList(" dLocal", " dokypay");
        String s = "I love dlocal DLocal dokypay Dokypay";
        String expected = "I love";

        assertEquals("StringUtil.remove fail", expected, StringUtil.remove(s, subs));
    }

    @Test
    public void remove1() {
        List<String> subs = Arrays.asList("dLocal", "dokypay");
        String s = "I love dlocal DLocal dokypay Dokypay";
        String expected = "I love    ";

        assertEquals("StringUtil.remove fail", expected, StringUtil.remove(s, subs));
    }

    @Test
    public void replace() {
        Map<String, String> map = new HashMap<>();
        map.put("dlocal", "dl");
        map.put("dokypay", "dk");
        map.put("PayTm", "tm");

        String s = "I love dlocal DLocal dokypay Dokypay";
        String expected = "I love dl dl dk dk";
        assertEquals("StringUtil.replace fail", expected, StringUtil.replace(s, map));
    }

    @Test
    public void substringLastTo() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringLastTo(s, sep));

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringLastTo(s, sep, true));

        s = "abc:d";
        sep = ":";
        expected = "abc";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringLastTo(s, sep));

        s = "abc:d:e";
        sep = ":";
        expected = "abc:d";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringLastTo(s, sep));
    }

    @Test
    public void substringLastFrom() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringLastFrom(s, sep));

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringLastFrom(s, sep, true));

        s = "abc:d";
        sep = ":";
        expected = "d";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringLastFrom(s, sep));

        s = "abc:d:e";
        sep = ":";
        expected = "e";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringLastFrom(s, sep));
    }

    @Test
    public void substringTo() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringTo(s, sep));

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringTo(s, sep, true));

        s = "abc:d";
        sep = ":";
        expected = "abc";
        assertEquals("StringUtil.substringTo fail", expected, StringUtil.substringTo(s, sep));
    }

    @Test
    public void substringFrom() {
        String s, expected, sep;

        s = "abc";
        sep = ":";
        expected = "";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringFrom(s, sep));

        s = "abc";
        sep = ":";
        expected = "abc";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringFrom(s, sep, true));

        s = "abc:d";
        sep = ":";
        expected = "d";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringFrom(s, sep));

        //分隔符为2个字符的场景
        s="jdbc:mysql://127.0.0.1:3306";
        sep="//";
        expected = "127.0.0.1:3306";
        assertEquals("StringUtil.substringFrom fail", expected, StringUtil.substringFrom(s, sep));

    }


    @Test
    public void substringToBlank() {
        String s = "Failed to invoke the method payout in the service com.wolf.business.pay.api.service.gateway.PayoutGatewayService. Tried 1 times of the providers [172.16.192.15:9080] (1/1) from the registry localhost:9090 on the consumer 47.111.7.95 using the dubbo version 2.7.3. Last error is: Invoke remote method timeout. method: payout, provider:";
        String expected = "Failed to invoke the method";

        String r2 = StringUtil.substringToBlank(s, 20);
        assertNotEquals("StringUtil.substringToBlank fail", expected, r2);

        String r1 = StringUtil.substringToBlank(s, 22);
        assertEquals("StringUtil.substringToBlank fail", expected, r1);

        String r3 = StringUtil.substringToBlank(s, 21);
        assertEquals("StringUtil.substringToBlank fail", expected, r3);

        String r4 = StringUtil.substringToBlank(s, 23);
        assertEquals("StringUtil.substringToBlank fail", expected, r4);
    }

    @Test
    public void startWith() {
        String s1 = " [";
        String s2 = " {";

        assertTrue("startWith fail", StringUtil.startsWith(s1, "[", true));
        assertFalse("startWith fail", StringUtil.startsWith(s2, "[", true));
    }

    @Test
    public void endsWith() {
        String s1 = "a_no ";
        String s2 = "b_id";

        assertTrue("endsWith fail", StringUtil.endsWith(s1, "_no", true));
        assertTrue("endsWith fail", StringUtil.endsWith(s2, "_id", true));
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
        assertFalse("StringUtil.isZero failed", StringUtil.isZero(null));
        assertFalse("StringUtil.isZero failed", StringUtil.isZero(""));

        assertTrue("StringUtil.isZero failed", StringUtil.isZero("0"));
        assertTrue("StringUtil.isZero failed", StringUtil.isZero("000"));
        assertTrue("StringUtil.isZero failed", StringUtil.isZero("00000000"));

        assertFalse("StringUtil.isZero failed", StringUtil.isZero("0101"));
        assertFalse("StringUtil.isZero failed", StringUtil.isZero("101"));
        assertFalse("StringUtil.isZero failed", StringUtil.isZero("abc"));
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
        assertFalse("StringUtil.contains failed", StringUtil.contains(null, "a"));
        assertFalse("StringUtil.contains failed", StringUtil.contains("", "a"));
        assertFalse("StringUtil.contains failed", StringUtil.contains(" ", "a"));

        assertTrue("StringUtil.contains failed", StringUtil.contains("abc", "a"));
        assertTrue("StringUtil.contains failed", StringUtil.contains("cabc", "ab"));

        assertFalse("StringUtil.contains failed", StringUtil.contains("cabc", "ac"));
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
