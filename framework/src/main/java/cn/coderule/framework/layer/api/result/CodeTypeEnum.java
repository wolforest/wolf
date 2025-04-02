package cn.coderule.framework.layer.api.result;

import cn.coderule.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum CodeTypeEnum implements CodeBasedEnum {
    ILLEGAL_ARGUMENT(50, "ILLEGAL_ARGUMENT"),
    NO_PERMISSION(40, "NO_PERMISSION"),
    NEED_LOGIN(30, "NEED_LOGIN"),
    BUSINESS_ERROR(20, "BUSINESS_ERROR"),
    SYSTEM_ERROR(10, "SYSTEM_ERROR"),

    SUCCESS(0, "SUCCESS女")
    ;

    private final int code;
    private final String name;

    CodeTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
