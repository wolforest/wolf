package com.wolf.mqclient.core.producer;

import com.wolf.common.lang.enums.CodeBasedEnum;
import lombok.Getter;

/**
 * com.wolf.common.lang.enums
 *
 * @author Wingle
 * @since 2019/9/29 4:51 PM
 **/
@Getter
public enum ProduceStateEnum implements CodeBasedEnum {
    SLAVE_NOT_AVAILABLE(100, "SLAVE_NOT_AVAILABLE"),

    FLUSH_SLAVE_TIMEOUT(70, "FLUSH_SLAVE_TIMEOUT"),
    FLUSH_DISK_TIMEOUT(60, "FLUSH_DISK_TIMEOUT"),
    TIMEOUT(50, "TIMEOUT"),

    WAIT_ASYNC_RESULT(30, "WAIT_ASYNC_RESULT"),
    FAILURE(20, "FAILURE"),
    SUCCESS(10, "SUCCESS"),

    NO_PRODUCER(1, "NO_PRODUCER"),
    ;

    private final int code;
    private final String name;

    ProduceStateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
