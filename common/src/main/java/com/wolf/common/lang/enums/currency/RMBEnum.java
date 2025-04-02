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
public enum RMBEnum implements CodeBasedEnum {
    SI(1008606, "丝"),
    HAO(1008605, "毫"),
    LI(1008604, "厘"),
    FEN(1008603, "分"),
    JIAO(1008602, "角"),
    YUAN(1008601,"元")
    ;

    private final int code;
    private final String name;

    RMBEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
