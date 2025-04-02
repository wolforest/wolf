package com.wolf.wolfno.model;

import com.wolf.common.convention.container.Context;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class WolfNoContext implements Context {
    private String name;
    private WolfNoStyleEnum style;
    private int type;
    private int datacenter;
    private int shard;

    private int step;
    private double rate;

    private LocalDateTime createTime;
}
