package cn.coderule.common.lang.enums.finance;

import lombok.Getter;
import cn.coderule.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum PrepayStrategyEnum implements CodeBasedEnum {
    NOT_ALLOW(3, "不允许"),
    ALLOW(2, "请允许"),
    CONTRACT(1, "约定还款")
    ;

    private final int code;
    private final String name;

    PrepayStrategyEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
