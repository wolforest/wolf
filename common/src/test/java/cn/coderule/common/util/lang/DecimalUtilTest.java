package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.number.DecimalUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class DecimalUtilTest {
    @Test
    public void testIsGreater() {
        Assertions.assertTrue(DecimalUtil.isGreater(new BigDecimal(2), new BigDecimal(1)));

        Assertions.assertFalse(DecimalUtil.isGreater(new BigDecimal(1), new BigDecimal(1)));
        Assertions.assertFalse(DecimalUtil.isGreater(new BigDecimal(1), new BigDecimal(2)));
    }

    @Test
    public void testIsGreaterOrEqual() {
        Assertions.assertTrue(DecimalUtil.isGreaterOrEqual(new BigDecimal(2), new BigDecimal(1)));
        Assertions.assertTrue(DecimalUtil.isGreaterOrEqual(new BigDecimal(1), new BigDecimal(1)));

        Assertions.assertFalse(DecimalUtil.isGreaterOrEqual(new BigDecimal(1), new BigDecimal(2)));
    }

    @Test
    public void testIsLess() {
        Assertions.assertTrue(DecimalUtil.isLess(new BigDecimal(1), new BigDecimal(2)));

        Assertions.assertFalse(DecimalUtil.isLess(new BigDecimal(1), new BigDecimal(1)));
        Assertions.assertFalse(DecimalUtil.isLess(new BigDecimal(2), new BigDecimal(1)));
    }

    @Test
    public void testIsLessOrEqual() {
        Assertions.assertTrue(DecimalUtil.isLessOrEqual(new BigDecimal(1), new BigDecimal(2)));
        Assertions.assertTrue(DecimalUtil.isLessOrEqual(new BigDecimal(1), new BigDecimal(1)));

        Assertions.assertFalse(DecimalUtil.isLessOrEqual(new BigDecimal(2), new BigDecimal(1)));
    }

    @Test
    public void testIsEqual() {
        BigDecimal one = BigDecimal.ONE;
        Assertions.assertTrue(DecimalUtil.isEqual(one, one));
        Assertions.assertTrue(DecimalUtil.isEqual(new BigDecimal(1), new BigDecimal(1)));

        Assertions.assertFalse(DecimalUtil.isEqual(new BigDecimal(2), new BigDecimal(1)));
        Assertions.assertFalse(DecimalUtil.isEqual(new BigDecimal(1), null));
        Assertions.assertFalse(DecimalUtil.isEqual(null, new BigDecimal(1)));
    }
}
