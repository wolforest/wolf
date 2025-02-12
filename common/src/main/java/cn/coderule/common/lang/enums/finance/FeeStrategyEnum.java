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
public enum FeeStrategyEnum implements CodeBasedEnum {
    INSTALLMENT(3, "后付待续费"),
    POST(2, "后付待续费"),
    PRE(1, "提前付手续费")
    ;

    private final int code;
    private final String name;

    FeeStrategyEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
