package com.wolf.wolfno;

import com.wolf.common.util.collection.CollectionUtil;
import com.wolf.wolfno.config.WolfNoConfig;
import com.wolf.wolfno.factory.WolfNoBuilder;
import com.wolf.wolfno.model.WolfNoStyleEnum;
import com.wolf.wolfno.wolfid.WolfIDContainer;
import org.springframework.jdbc.core.JdbcTemplate;

public class WolfNo {
    private final WolfNoConfig config;

    public WolfNo(WolfNoConfig config, JdbcTemplate jdbcTemplate) {
        this.config = config;

        WolfIDContainer.singleton().init(config, jdbcTemplate);
    }

    public void init() {
        if (CollectionUtil.isEmpty(this.config.getNoList())) {
            return;
        }

    }

    /**
     * WolfNo builder api
     *  -> create WolfNoBuilder
     *  -> WolfNoBuilder.build()
     *  -> WolfNoFactory.create()
     *
     * @return WolfNoBuilder
     */
    public WolfNoBuilder builder() {
        return new WolfNoBuilder();
    }

    public String create(String name, int type) {
        return new WolfNoBuilder()
            .name(name)
            .style(WolfNoStyleEnum.DAY_ID_24)
            .type(type)
//            .datacenter(0)
//            .shard(123)
            .build();
    }

    public String create(String name, int type, int shard) {
        return new WolfNoBuilder()
            .name(name)
            .style(WolfNoStyleEnum.DAY_ID_24)
            .type(type)
            .shard(shard)
            .build();
    }

}
