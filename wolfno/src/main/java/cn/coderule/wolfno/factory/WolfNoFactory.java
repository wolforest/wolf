package cn.coderule.wolfno.factory;

import cn.coderule.wolfno.model.WolfNoContext;
import cn.coderule.wolfno.strategy.EveryDay24;
import cn.coderule.wolfno.wolfid.WolfIDContainer;
import java.time.LocalDateTime;

public class WolfNoFactory {

    private final WolfNoContext context;

    public WolfNoFactory(WolfNoContext context) {
        this.context = context;
    }

    public String create() {
        LocalDateTime now = LocalDateTime.now();
        context.setCreateTime(now);

        String wolfID = WolfIDContainer.singleton().getWolfID(context);

        WolfNoCreator creator = findCreatorByStyle();
        creator.setContext(context);
        creator.setWolfID(wolfID);

        return creator.create();
    }

    private WolfNoCreator findCreatorByStyle() {
        return new EveryDay24();
    }
}
