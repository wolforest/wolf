package cn.coderule.ddd.layer;

import cn.coderule.common.lang.exception.BusinessException;

public interface Transaction {
    void begin() throws BusinessException;
    void commit() throws BusinessException;
    void rollback() throws BusinessException;
}
