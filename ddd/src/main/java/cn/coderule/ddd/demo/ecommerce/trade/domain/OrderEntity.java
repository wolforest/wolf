package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.trade.api.Order;
import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPlacementCommand;
import cn.coderule.ddd.demo.ecommerce.trade.api.event.OrderPaid;
import java.math.BigDecimal;

public class OrderEntity extends Order {
    public void place(BigDecimal price, OrderPlacementCommand command) {

        addEvent(new OrderPaid());
    }

    public void cancel() {

    }

    public void pay() {

    }
}
