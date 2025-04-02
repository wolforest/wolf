package com.wolf.wolfno.model;

import lombok.Getter;
import com.wolf.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum WolfNoStyleEnum implements CodeBasedEnum {
    MS_ID_32(20, "DEFAULT"),
    DAY_ID_24(10, "24bits: 240801 000 9 1024  00 9999999"),
    ;

    private final int code;
    private final String name;

    WolfNoStyleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
