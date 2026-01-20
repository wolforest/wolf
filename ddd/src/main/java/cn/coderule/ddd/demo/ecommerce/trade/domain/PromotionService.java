package cn.coderule.ddd.demo.ecommerce.trade.domain;

import cn.coderule.ddd.demo.ecommerce.ump.api.PromotionCommand;
import java.math.BigDecimal;

public interface PromotionService {
    BigDecimal apply(PromotionCommand command);
}
