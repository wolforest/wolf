package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.trade.api.Order;
import cn.coderule.ddd.demo.ecommerce.trade.api.OrderPlacementCommand;
import java.math.BigDecimal;

public class OrderEntity extends Order {
    public void place(BigDecimal price, OrderPlacementCommand command) {

    }
}
