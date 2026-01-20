package cn.coderule.ddd.demo.ecommerce.trade.domain;

public interface OrderRepository {
    void save(OrderEntity order);
    OrderEntity findById(String id);
}
