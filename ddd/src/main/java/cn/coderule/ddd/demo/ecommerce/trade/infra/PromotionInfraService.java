package cn.coderule.ddd.demo.ecommerce.trade.infra;

import cn.coderule.ddd.demo.ecommerce.trade.domain.PromotionService;
import cn.coderule.ddd.demo.ecommerce.ump.api.PromotionCommand;
import java.math.BigDecimal;

public class PromotionInfraService implements PromotionService {
    @Override
    public BigDecimal apply(PromotionCommand command) {
        return null;
    }
}
