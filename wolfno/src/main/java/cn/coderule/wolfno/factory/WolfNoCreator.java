package cn.coderule.wolfno.factory;

import cn.coderule.wolfno.model.WolfNoContext;

public interface WolfNoCreator {

    void setContext(WolfNoContext context);
    void setWolfID(String ID);

    WolfNoContext getContext();
    String getWolfID();

    String create();
}
