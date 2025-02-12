package cn.coderule.common.util.lang;

import java.math.BigDecimal;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/2/12 12:19 下午
 **/
public class NumberUtil {
    public static Number add(Number a, Number b) {
        return add(a, b, 4);
    }

    public static Number add(Number a, Number b, int scale) {
        if(a instanceof BigDecimal && b instanceof BigDecimal) {
            BigDecimal result = ((BigDecimal) a).add((BigDecimal) b);
            return DecimalUtil.scale(result, scale);
        } else if(a instanceof Double || b instanceof Double) {
            return a.doubleValue() + b.doubleValue();
        } else if(a instanceof Float || b instanceof Float) {
            return a.floatValue() + b.floatValue();
        } else if(a instanceof Long || b instanceof Long) {
            return a.longValue() + b.longValue();
        } else if(a instanceof Integer || b instanceof Integer) {
            return a.intValue() + b.intValue();
        } else if(a instanceof Short || b instanceof Short) {
            return a.intValue() + b.intValue();
        } else if(a instanceof Byte || b instanceof Byte) {
            return a.intValue() + b.intValue();
        }

        return null;
    }
}
