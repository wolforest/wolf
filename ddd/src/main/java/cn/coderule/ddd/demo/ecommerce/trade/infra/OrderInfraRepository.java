package cn.coderule.ddd.demo.ecommerce.trade.infra;

import cn.coderule.ddd.demo.ecommerce.trade.domain.OrderEntity;
import cn.coderule.ddd.demo.ecommerce.trade.domain.OrderRepository;

public class OrderInfraRepository implements OrderRepository {
    private final EventPublisher eventPublisher;

    public OrderInfraRepository() {
        this.eventPublisher = new EventPublisher();
    }

    public OrderEntity findById(String orderId) {
        return new OrderEntity();
    }

    @Override
    public void save(OrderEntity order) {

        eventPublisher.publish(order.getEventList());
    }
}
