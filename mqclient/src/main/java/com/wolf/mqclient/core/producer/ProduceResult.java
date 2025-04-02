package com.wolf.mqclient.core.producer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * com.wolf.mqclient.domain.dto
 *
 * @author Wingle
 * @since 2021/12/1 上午12:26
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProduceResult {
    private ProduceStateEnum state;

    private String messageId;
    private Long offset;

    public static ProduceResult SUCCESS() {
        return ProduceResult.builder()
                .state(ProduceStateEnum.SUCCESS)
                .build();
    }

    public static ProduceResult FAILURE() {
        return ProduceResult.builder()
                .state(ProduceStateEnum.FAILURE)
                .build();
    }

    public static ProduceResult NO_PRODUCER() {
        return ProduceResult.builder()
                .state(ProduceStateEnum.NO_PRODUCER)
                .build();
    }

    public static ProduceResult WAIT_ASYNC_RESULT(String messageId) {
        return ProduceResult.builder()
                .messageId(messageId)
                .state(ProduceStateEnum.WAIT_ASYNC_RESULT)
                .build();
    }

    public boolean isSuccess() {
        return ProduceStateEnum.SUCCESS.equals(state);
    }
}
