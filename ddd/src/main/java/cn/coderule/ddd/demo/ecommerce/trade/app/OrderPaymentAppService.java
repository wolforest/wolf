package cn.coderule.ddd.demo.ecommerce.trade.app;

import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPaymentCommand;
import cn.coderule.ddd.demo.ecommerce.trade.domain.OrderPaymentService;
import cn.coderule.ddd.demo.ecommerce.trade.domain.OrderRepository;
import cn.coderule.ddd.demo.ecommerce.trade.infra.OrderInfraRepository;

public class OrderPaymentAppService {
    private final OrderPaymentService orderPlacementService;

    public OrderPaymentAppService() {
        this.orderPlacementService = new OrderPaymentService();

        OrderRepository orderRepository = new OrderInfraRepository();
        this.orderPlacementService.inject(orderRepository);
    }

    public void pay(OrderPaymentCommand command) {
        this.orderPlacementService.pay(command);
    }
}
