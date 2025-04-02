package com.wolf.common.lang.enums.finance;

import lombok.Getter;
import com.wolf.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum RepayStrategyEnum implements CodeBasedEnum {
    UNKNOWN(3, "未知"),
    ONE_OFF(2, "一次性还本付息"),
    CONTRACT(1, "约定还款")
    ;

    private final int code;
    private final String name;

    RepayStrategyEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
