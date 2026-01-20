package cn.coderule.ddd.demo.ecommerce.inventory.api;

public interface InventoryService {
    boolean reserve(ReserveCommand command);
}
