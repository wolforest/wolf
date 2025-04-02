package com.wolf.framework.layer.web.health;

import com.wolf.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * @author weixing
 * @since 2023/10/12 17:30
 */
@Getter
public enum AppHealthStatusEnum implements CodeBasedEnum {
    UP(200, "UP"),
    DOWN(503, "DOWN"),
    OUT_OF_SERVICE(502, "OUT_OF_SERVICE"),
    UNKNOWN(500, "UNKNOWN")
    ;

    private final int code;
    private final String name;

    AppHealthStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
