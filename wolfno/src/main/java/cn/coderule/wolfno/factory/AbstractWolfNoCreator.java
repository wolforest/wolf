package cn.coderule.wolfno.factory;

import cn.coderule.wolfno.model.WolfNoContext;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Data;

@Data
public abstract class AbstractWolfNoCreator implements WolfNoCreator {
    protected WolfNoContext context;
    protected String wolfID;

    protected int formatDatacenter() {
        return 0;
    }

    protected String formatShard() {
        int shard = context.getShard();
        if (shard == 0) {
            return "93" + ThreadLocalRandom.current().nextInt(10, 99);
        }

        return String.format("%04d", shard);
    }

}
