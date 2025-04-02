package cn.coderule.wolfno.wolfid;

import cn.coderule.common.util.lang.BeanUtil;
import cn.coderule.wolfno.model.WolfNoContext;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Data;

@Data
public class WolfID implements Serializable {
    private boolean hasStandBy = false;

    private String name;
    private int shard;
    private int idShard;
    private int step;

    private AtomicInteger currentID;
    private int maxID;
    private double rate;

    private LocalDateTime createTime;

    public boolean isValid() {
        return maxID > currentID.intValue();
    }

    public static WolfID fromContext(WolfNoContext context) {
        WolfID wolfID = new WolfID();

        BeanUtil.copyProperties(context, wolfID);

        return wolfID;
    }
}
