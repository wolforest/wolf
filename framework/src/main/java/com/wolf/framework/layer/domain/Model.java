package com.wolf.framework.layer.domain;

import java.io.Serializable;

/**
 * @author weixing
 * @since 2022/8/30 15:52
 */
public interface Model extends Serializable {
    Integer getVersion();

    void setVersion(Integer version);
}
