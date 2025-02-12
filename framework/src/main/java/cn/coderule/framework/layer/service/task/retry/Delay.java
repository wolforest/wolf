package cn.coderule.framework.layer.service.task.retry;

import cn.coderule.framework.layer.api.dto.DTO;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NonNull;

/**
 * com.wolf.framework.layer.task.retry
 *
 * @author Wingle
 * @since 2021/12/13 上午2:08
 **/
@Data
public class Delay implements DTO {
    public static final Delay NO_DELAY = new Delay(-1L);
    private final long seconds;
    private final LocalDateTime since;
    private final LocalDateTime at;

    public Delay(Long seconds) {
        this(seconds, LocalDateTime.now());
    }

    public Delay(Long seconds, @NonNull LocalDateTime since) {
        this.seconds = null != seconds ? seconds : 0L;
        this.since = since;
        this.at = since.plusSeconds(seconds);
    }

    public boolean isValid() {
        return seconds > 0;
    }
}
