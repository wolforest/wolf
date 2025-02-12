package cn.coderule.common.util.lang;

import cn.coderule.common.util.lang.DecimalUtil;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;


public class DecimalUtilTest {
    @Test
    public void testIsGreater() {
        Assert.assertTrue(DecimalUtil.isGreater(new BigDecimal(2), new BigDecimal(1)));

        Assert.assertFalse(DecimalUtil.isGreater(new BigDecimal(1), new BigDecimal(1)));
        Assert.assertFalse(DecimalUtil.isGreater(new BigDecimal(1), new BigDecimal(2)));
    }

    @Test
    public void testIsGreaterOrEqual() {
        Assert.assertTrue(DecimalUtil.isGreaterOrEqual(new BigDecimal(2), new BigDecimal(1)));
        Assert.assertTrue(DecimalUtil.isGreaterOrEqual(new BigDecimal(1), new BigDecimal(1)));

        Assert.assertFalse(DecimalUtil.isGreaterOrEqual(new BigDecimal(1), new BigDecimal(2)));
    }

    @Test
    public void testIsLess() {
        Assert.assertTrue(DecimalUtil.isLess(new BigDecimal(1), new BigDecimal(2)));

        Assert.assertFalse(DecimalUtil.isLess(new BigDecimal(1), new BigDecimal(1)));
        Assert.assertFalse(DecimalUtil.isLess(new BigDecimal(2), new BigDecimal(1)));
    }

    @Test
    public void testIsLessOrEqual() {
        Assert.assertTrue(DecimalUtil.isLessOrEqual(new BigDecimal(1), new BigDecimal(2)));
        Assert.assertTrue(DecimalUtil.isLessOrEqual(new BigDecimal(1), new BigDecimal(1)));

        Assert.assertFalse(DecimalUtil.isLessOrEqual(new BigDecimal(2), new BigDecimal(1)));
    }

    @Test
    public void testIsEqual() {
        BigDecimal one = BigDecimal.ONE;
        Assert.assertTrue(DecimalUtil.isEqual(one, one));
        Assert.assertTrue(DecimalUtil.isEqual(new BigDecimal(1), new BigDecimal(1)));

        Assert.assertFalse(DecimalUtil.isEqual(new BigDecimal(2), new BigDecimal(1)));
        Assert.assertFalse(DecimalUtil.isEqual(new BigDecimal(1), null));
        Assert.assertFalse(DecimalUtil.isEqual(null, new BigDecimal(1)));
    }
}
