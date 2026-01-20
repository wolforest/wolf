package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPaymentCommand;

public class OrderPaymentService {
    private OrderRepository orderRepository;

    public void inject(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void pay(OrderPaymentCommand command) {
        OrderEntity order = orderRepository.findById(command.getOrderId());

        order.pay();
        orderRepository.save(order);
    }
}
