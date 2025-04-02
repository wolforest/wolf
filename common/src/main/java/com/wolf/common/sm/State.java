package com.wolf.common.sm;

import java.io.Serializable;

/**
 * com.wolf.common.framework.layer.domain
 *
 * @author Wingle
 * @since 2018/12/26 11:45 PM
 **/
public interface State extends Serializable {
    int getCode();
    String getName();
}
