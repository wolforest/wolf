package cn.coderule.ddd.demo.ecommerce.trade.infra;

import cn.coderule.ddd.demo.ecommerce.inventory.api.ReserveCommand;
import cn.coderule.ddd.demo.ecommerce.trade.domain.InventoryService;

public class InventoryInfraService implements InventoryService {
    @Override
    public boolean reserve(ReserveCommand command) {
        return false;
    }
}
