package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.inventory.api.ReserveCommand;

public interface InventoryService {
    boolean reserve(ReserveCommand command);
}
