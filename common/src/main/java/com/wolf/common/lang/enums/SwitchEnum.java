package com.wolf.common.lang.enums;

import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum SwitchEnum implements CodeBasedEnum {
    OFF(2, "OFF"),
    ON(1, "ON");

    private final int code;
    private final String name;

    SwitchEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
