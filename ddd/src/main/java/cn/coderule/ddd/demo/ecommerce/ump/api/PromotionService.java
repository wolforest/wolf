package cn.coderule.ddd.demo.ecommerce.ump.api;

import java.math.BigDecimal;

public interface PromotionService {
    BigDecimal apply(PromotionCommand command);
}
