package com.wolf.httpclient.enums;

import lombok.Getter;
import com.wolf.common.lang.enums.CodeBasedEnum;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2020/11/18 1:36 下午
 **/
@Getter
public enum ContentTypeEnum implements CodeBasedEnum {
    JOSE(100, "jose", "application/jose"),

    CSV(13, "csv", "text/csv; charset=utf-8"),
    MSWORD(12, "msword", "application/msword"),
    PDF(11, "pdf", "application/pdf"),
    FILE(10, "file", "multipart/form-data"),

    STREAM(6, "stream", "application/octet-stream"),
    FORM(5, "form", "application/x-www-form-urlencoded"),
    HTML(4, "html", "text/html; charset=utf-8"),
    TEXT(3, "text", "text/plain; charset=utf-8"),
    XML(2, "xml", "text/xml; charset=utf-8"),
    JSON(1, "json", "application/json; charset=utf-8"),
    ;

    private final int code;
    private final String name;
    private final String header;

    ContentTypeEnum(int code, String name, String header) {
        this.code = code;
        this.name = name;
        this.header = header;
    }
}
