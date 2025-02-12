package cn.coderule.common.lang.constant;

import java.math.BigDecimal;

/**
 * com.wolf.common.lang.constant
 *
 * @author Wingle
 * @since 2021/11/15 下午11:53
 **/
public class DecimalConst implements Constant {
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    public static final BigDecimal ONE = BigDecimal.ONE;
    public static final BigDecimal TEN = BigDecimal.TEN;

    public static final BigDecimal HUNDRED = BigDecimal.valueOf(100);
    public static final BigDecimal THOUSAND = BigDecimal.valueOf(1000);
    public static final BigDecimal TEN_THOUSANDS = BigDecimal.valueOf(10000);
    public static final BigDecimal THOUSAND_THOUSANDS = BigDecimal.valueOf(100000);
    public static final BigDecimal MILLION = BigDecimal.valueOf(1000000);
    public static final BigDecimal BILLION = BigDecimal.valueOf(1000000000);
}
