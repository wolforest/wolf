package cn.coderule.httpclient.cookie;

import lombok.Getter;
import cn.coderule.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2020/11/18 1:36 下午
 **/
@Getter
public enum CookiePolicyEnum implements CodeBasedEnum {
    /**
     * default policy
     */
    ACCEPT_NONE(30, "ACCEPT_NONE"),

    /**
     *
     */
    ACCEPT_ALL(20, "ACCEPT_ALL"),

    /**
     *
     */
    ACCEPT_ORIGINAL_SERVER(10, "ACCEPT_ORIGINAL_SERVER");

    private final int code;
    private final String name;

    CookiePolicyEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
