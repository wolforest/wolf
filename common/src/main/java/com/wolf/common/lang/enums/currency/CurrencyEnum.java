package com.wolf.common.lang.enums.currency;

import lombok.Getter;
import com.wolf.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.model.enums
 *
 * @author Wingle
 * @since 2019/10/15 12:39 下午
 **/
@Getter
public enum CurrencyEnum implements CodeBasedEnum {
    MXV(979, "MXV"),
    MXN(484, "MXN"),
    IDR(360, "IDR"),
    NGN(566, "NGN"),
    BRL(986, "BRL"),
    INR(356, "INR"),
    CNY(156, "CNY"),
    USD(840,"USD"),
    EUR(978, "EUR"),
    GBP(826, "GBP"),
    JPY(392, "JPY"),
    HKD(344, "HKD"),
    AUD(36, "AUD"),
    CAD(124, "CAD"),
    CNH(156, "CNH"),
    NZD(554, "NZD"),
    CHF(756, "CHF"),
    AED(784, "AED"),
    SAR(682, "SAR"),
    SGD(702, "SGD"),

    ;

    private final int code;
    private final String name;

    CurrencyEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
