package cn.coderule.ddd.demo.ecommerce.trade;

import cn.coderule.ddd.demo.ecommerce.trade.api.OrderPlacementResult;
import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPaymentCommand;
import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPlacementCommand;
import cn.coderule.ddd.demo.ecommerce.trade.app.OrderPaymentAppService;
import cn.coderule.ddd.demo.ecommerce.trade.app.OrderPlacementAppService;

public class Main {
    public static void main(String[] args) {
        placeOrder();
        payOrder();
    }

    private static void placeOrder() {
        OrderPlacementCommand command = new OrderPlacementCommand();
        OrderPlacementAppService appService = new OrderPlacementAppService();

        OrderPlacementResult result = appService.place(command);
        System.out.println(result);
    }

    private static void payOrder() {
        OrderPaymentCommand command = new OrderPaymentCommand();
        OrderPaymentAppService appService = new OrderPaymentAppService();

        appService.pay(command);
    }
}
