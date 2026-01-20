package cn.coderule.ddd.layer;

import cn.coderule.common.lang.exception.BusinessException;

public class MockTransaction implements Transaction {
    @Override
    public void begin() throws BusinessException {

    }

    @Override
    public void commit() throws BusinessException {

    }

    @Override
    public void rollback() throws BusinessException {

    }
}
