package com.wolf.framework.layer.web.auth.model;

import com.wolf.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum SpaceRoleEnum implements CodeBasedEnum {
    GUEST(110, "GUEST"),

    TEAM_MEMBER(20, "TEAM_MEMBER"),
    TEAM_OWNER(10, "TEAM_OWNER"),

    PRIVATE(1, "PRIVATE")
    ;

    private final int code;
    private final String name;

    SpaceRoleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
