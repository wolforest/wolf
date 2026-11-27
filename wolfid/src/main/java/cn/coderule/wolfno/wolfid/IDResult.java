package cn.coderule.wolfno.wolfid;

import java.io.Serializable;
import lombok.Data;

@Data
public class IDResult implements Serializable {
    private int shard;
    private int maxID;
    private int step;

}
