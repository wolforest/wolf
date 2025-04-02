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
public enum AddressSchemaEnum implements CodeBasedEnum {
    IPv4(4, "IPv4"),
    IPv6(3, "IPv6"),
    DOMAIN_NAME(2, "DOMAIN_NAME"),
    UNRECOGNIZED(1, "UNRECOGNIZED")
    ;

    private final int code;
    private final String name;

    AddressSchemaEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
