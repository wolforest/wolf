package cn.coderule.common.lang.enums.unit;

import cn.coderule.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 * range(1~100)
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum DigitalUnitEnum implements CodeBasedEnum {
    PB(60, "P"),
    TB(50, "T"),
    GB(40, "G"),
    MB(30, "M"),
    KB(20, "K"),
    B(10, "bytes")
    ;

    private final int code;
    private final String name;

    DigitalUnitEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
