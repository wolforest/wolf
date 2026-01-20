package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.trade.api.Order;

public interface OrderRepository {
    void save(OrderEntity order);
}
