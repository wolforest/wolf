package cn.coderule.ddd.demo.ecommerce.inventory.api;

import java.io.Serializable;
import lombok.Data;

@Data
public class ReserveCommand implements Serializable {
    private String orderId;
    private String inventoryId;
    private int quantity;
}
