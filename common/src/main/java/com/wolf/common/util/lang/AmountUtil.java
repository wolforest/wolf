package com.wolf.common.util.lang;

import java.math.BigDecimal;

/**
 * com.wolf.business.pay.biz.channel.core.util
 *
 * @author Wingle
 * @since 2021/2/2 4:42 下午
 **/
public class AmountUtil {
    private static final int DEFAULT_SCALE = 2;

    public static BigDecimal getAmount(BigDecimal amount) {
        return getAmount(amount, DEFAULT_SCALE);
    }
    
    public static BigDecimal getAmount(BigDecimal amount, int scale) {
        amount = DecimalUtil.scale(amount, scale);

        return amount;
    }
}
