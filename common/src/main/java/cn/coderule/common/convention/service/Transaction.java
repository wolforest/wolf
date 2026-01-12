package cn.coderule.common.convention.service;

public interface Transaction {
    void begin() throws Exception;
    void commit() throws Exception;
    void rollback();
}
