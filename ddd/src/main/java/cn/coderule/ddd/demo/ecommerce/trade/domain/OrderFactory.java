package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.common.util.lang.string.StringUtil;


public class OrderFactory {
    public String generateOrderId() {
        return StringUtil.uuid();
    }

    public OrderEntity createOrder() {
        return new OrderEntity();
    }
}
