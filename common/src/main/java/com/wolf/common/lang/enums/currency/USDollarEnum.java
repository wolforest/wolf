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
public enum USDollarEnum implements CodeBasedEnum {
    PENNY(1000102, "penny"),
    DOLLAR(1000101,"dollar")
    ;

    private final int code;
    private final String name;

    USDollarEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
