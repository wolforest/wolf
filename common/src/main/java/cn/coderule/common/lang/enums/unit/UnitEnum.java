package cn.coderule.common.lang.enums.unit;

import lombok.Getter;
import cn.coderule.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.common.lang.enums
 * range(1~100)
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum UnitEnum implements CodeBasedEnum {
    PERCENTAGE(2, "percentage"),
    NONE(1, "none unit")
    ;

    private final int code;
    private final String name;

    UnitEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
