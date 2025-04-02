package cn.coderule.common.lang.enums.common;

import cn.coderule.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum GenderEnum implements CodeBasedEnum {
    UNKNOWN(0, "未知"),
    MALE(1, "男"),
    FEMALE(2, "女")
    ;

    private final int code;
    private final String name;

    GenderEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
