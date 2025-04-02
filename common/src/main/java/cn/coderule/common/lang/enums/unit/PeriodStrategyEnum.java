package cn.coderule.common.lang.enums.unit;

import cn.coderule.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 * range(100~120)
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum PeriodStrategyEnum implements CodeBasedEnum {
    CLOSE_CLOSE(4, "close close"),
    CLOSE_OPEN(3, "close open"),
    OPEN_OPEN(2, "open open"),
    OPEN_CLOSE(1, "open close")
    ;

    private final int code;
    private final String name;

    PeriodStrategyEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
