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
public enum SameSiteEnum implements CodeBasedEnum {
    STRICT(30, "Strict"),
    LAX(20, "Lax"),
    NONE(10, "None"),
    ;

    private final int code;
    private final String name;

    SameSiteEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
