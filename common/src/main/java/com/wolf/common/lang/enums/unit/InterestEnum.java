package com.wolf.common.lang.enums.unit;

import lombok.Getter;
import com.wolf.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.common.lang.enums
 * range(120~140)
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum InterestEnum implements CodeBasedEnum {
    COMPOUND_PER_YEAR(133, "compound interest rate per year"),
    COMPOUND_PER_MONTH(132, "compound interest rate per month"),
    COMPOUND_PER_WEEK(131, "compound interest rate per week"),
    COMPOUND_PER_DAY(130, "compound interest rate per day"),

    RATE_PER_YEAR(126, "rate per year"),
    RATE_PER_MONTH(125, "rate per month"),
    RATE_PER_WEEK(124, "rate per week"),
    RATE_PER_DAY(123, "rate per day"),

    FIXED_AMOUNT(121, "fixed amount"),
    FIXED_RATE(120, "fixed rate")
    ;

    private final int code;
    private final String name;

    InterestEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
