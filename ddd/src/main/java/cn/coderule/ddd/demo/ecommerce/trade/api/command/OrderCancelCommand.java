package cn.coderule.ddd.demo.ecommerce.trade.api.command;

import java.io.Serializable;
import lombok.Data;

@Data
public class OrderCancelCommand implements Serializable {
    private String buyerId;
    private String sellerId;

    private String orderId;

}
