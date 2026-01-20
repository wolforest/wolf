package cn.coderule.ddd.demo.ecommerce.trade.app;

import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPlacementCommand;
import cn.coderule.ddd.demo.ecommerce.trade.api.OrderPlacementResult;
import cn.coderule.ddd.demo.ecommerce.trade.domain.InventoryService;
import cn.coderule.ddd.demo.ecommerce.trade.domain.OrderPlacementService;
import cn.coderule.ddd.demo.ecommerce.trade.domain.OrderRepository;
import cn.coderule.ddd.demo.ecommerce.trade.domain.ProductService;
import cn.coderule.ddd.demo.ecommerce.trade.domain.PromotionService;
import cn.coderule.ddd.demo.ecommerce.trade.infra.InventoryInfraService;
import cn.coderule.ddd.demo.ecommerce.trade.infra.OrderInfraRepository;
import cn.coderule.ddd.demo.ecommerce.trade.infra.ProductInfraService;
import cn.coderule.ddd.demo.ecommerce.trade.infra.PromotionInfraService;
import cn.coderule.ddd.layer.MockTransaction;

public class OrderPlacementAppService {
    private final OrderPlacementService orderPlacementService;

    public OrderPlacementAppService() {
        orderPlacementService = new OrderPlacementService();

        ProductService productService = new ProductInfraService();
        InventoryService inventoryService = new InventoryInfraService();
        PromotionService promotionService = new PromotionInfraService();
        OrderRepository orderRepository = new OrderInfraRepository();

        orderPlacementService.inject(productService, inventoryService, promotionService, orderRepository);
    }

    public OrderPlacementResult place(OrderPlacementCommand command) {
        MockTransaction transaction = new MockTransaction();

        try {
            transaction.begin();
            OrderPlacementResult result = orderPlacementService.place(command);
            transaction.commit();

            return result;
        } catch (Exception e) {
            transaction.rollback();
        }

        return OrderPlacementResult.failure();
    }
}
