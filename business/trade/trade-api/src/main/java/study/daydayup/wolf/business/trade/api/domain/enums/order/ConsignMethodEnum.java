package study.daydayup.wolf.business.trade.api.domain.enums.order;

import lombok.Getter;
import study.daydayup.wolf.common.lang.enums.CodeBasedEnum;

/**
 * study.daydayup.wolf.business.trade.api.domain.enums
 *
 * @author Wingle
 * @since 2019/10/5 6:55 PM
 **/
@Getter
public enum ConsignMethodEnum implements CodeBasedEnum {
    SELF_FETCH(3, "自提"),
    EXPRESS(2, "快递"),
    NO_LOGISTICS(1, "无需物流")
    ;

    private int   code;
    private String name;
    ConsignMethodEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
