package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.inventory.api.ReserveCommand;
import cn.coderule.ddd.demo.ecommerce.product.api.Product;
import cn.coderule.ddd.demo.ecommerce.trade.api.command.OrderPlacementCommand;
import cn.coderule.ddd.demo.ecommerce.trade.api.OrderPlacementResult;
import cn.coderule.ddd.demo.ecommerce.trade.api.TradeException;
import cn.coderule.ddd.demo.ecommerce.ump.api.PromotionCommand;
import java.math.BigDecimal;

public class OrderPlacementService {
    private final OrderFactory orderFactory = new OrderFactory();

    private ProductService productService;
    private InventoryService inventoryService;
    private PromotionService promotionService;
    private OrderRepository orderRepository;

    public void inject(
        ProductService productService,
        InventoryService inventoryService,
        PromotionService promotionService,
        OrderRepository orderRepository
    ) {
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.promotionService = promotionService;
        this.orderRepository = orderRepository;
    }

    public OrderPlacementResult place(OrderPlacementCommand command) {
        OrderEntity order = orderFactory.createOrder();

        Product product = productService.getSellableProduct(command.getProductId());
        if (product == null) {
            throw new TradeException();
        }

        ReserveCommand reserveCommand = new ReserveCommand();
        if (!inventoryService.reserve(reserveCommand)) {
            throw new TradeException();
        }

        PromotionCommand promotionCommand = new PromotionCommand();
        BigDecimal price = promotionService.apply(promotionCommand);

        order.place(price, command);
        orderRepository.save(order);

        return new OrderPlacementResult();
    }
}
