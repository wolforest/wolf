package com.wolf.wolfno.factory;

import com.wolf.wolfno.model.WolfNoContext;
import java.time.LocalDateTime;

public interface WolfNoCreator {

    void setContext(WolfNoContext context);
    void setWolfID(String ID);

    WolfNoContext getContext();
    String getWolfID();

    String create();
}
