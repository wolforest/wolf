package cn.coderule.ddd.demo.ecommerce.trade.api;

import java.io.Serializable;
import lombok.Data;

@Data
public class OrderPlacementResult implements Serializable {
    private boolean success;

    private String orderId;
    private String orderStatus;


    public static OrderPlacementResult failure() {
        return new OrderPlacementResult();
    }

}
