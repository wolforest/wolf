package com.wolf.wolfno.factory;

import com.wolf.wolfno.model.WolfNoStyleEnum;
import com.wolf.wolfno.model.WolfNoContext;

public class WolfNoBuilder {
    private final WolfNoContext context;

    public WolfNoBuilder() {
        this.context = new WolfNoContext();
    }

    public WolfNoBuilder style(WolfNoStyleEnum style) {
        context.setStyle(style);
        return this;
    }

    public WolfNoBuilder name(String name) {
        context.setName(name);

        return this;
    }

    public WolfNoBuilder type(int type) {
        context.setType(type);

        return this;
    }

    public WolfNoBuilder datacenter(int datacenter) {
        context.setDatacenter(datacenter);

        return this;
    }

    public WolfNoBuilder shard(int shard) {
        context.setShard(shard);

        return this;
    }

    public WolfNoBuilder step(int step) {
        context.setStep(step);

        return this;
    }

    public WolfNoBuilder rate(int rate) {
        context.setRate(rate);

        return this;
    }

    public String build() {
        WolfNoFactory factory = new WolfNoFactory(context);
        return factory.create();
    }


}
