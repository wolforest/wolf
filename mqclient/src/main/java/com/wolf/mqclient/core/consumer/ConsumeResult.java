package com.wolf.mqclient.core.consumer;

import com.wolf.mqclient.core.message.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * com.wolf.mqclient.domain.dto
 *
 * @author Wingle
 * @since 2021/12/1 上午2:35
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConsumeResult {
    private ConsumeStateEnum state;

    //optional
    private Message lastRecord;

    public static ConsumeResult SUCCESS() {
        return ConsumeResult.builder()
                .state(ConsumeStateEnum.SUCCESS)
                .build();
    }

    public static ConsumeResult SUCCESS(Message lastRecord) {
        return ConsumeResult.builder()
                .state(ConsumeStateEnum.SUCCESS)
                .lastRecord(lastRecord)
                .build();
    }

    public static ConsumeResult FAILURE() {
        return ConsumeResult.builder()
                .state(ConsumeStateEnum.FAILURE)
                .build();
    }

    public static ConsumeResult LATER() {
        return ConsumeResult.builder()
                .state(ConsumeStateEnum.LATER)
                .build();
    }
}
