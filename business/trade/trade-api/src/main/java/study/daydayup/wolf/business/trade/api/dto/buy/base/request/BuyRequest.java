package study.daydayup.wolf.business.trade.api.dto.buy.base.request;

import lombok.Data;
import study.daydayup.wolf.business.trade.api.domain.state.TradeState;
import study.daydayup.wolf.business.trade.api.domain.vo.BuyerMemo;
import study.daydayup.wolf.business.trade.api.domain.vo.buy.Buyer;
import study.daydayup.wolf.business.trade.api.domain.vo.OrderAddress;
import study.daydayup.wolf.business.trade.api.domain.vo.buy.Goods;
import study.daydayup.wolf.business.trade.api.domain.vo.buy.Seller;
import study.daydayup.wolf.business.trade.api.domain.vo.buy.TradeEnv;
import study.daydayup.wolf.framework.layer.api.Request;
import study.daydayup.wolf.business.trade.api.domain.enums.TradeTypeEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * study.daydayup.wolf.business.trade.api.dto.buy.base.request
 *
 * @author Wingle
 * @since 2019/10/9 1:43 下午
 **/
@Data
public class BuyRequest implements Request {
    private String tradeNo;
    private Buyer buyer;
    private Seller seller;

    /**
     * @see TradeTypeEnum
     */
    private Integer tradeType;
    private TradeState tradeState;
    private String relatedTradeNo;

    private String source;
    private TradeEnv env;
    private String tags;

    private OrderAddress address;
    private BuyerMemo buyerMemo;

    @NotNull
    private List<GoodsRequest> goodsRequest;
    private Goods goods;
    private UmpRequest umpRequest;

    private boolean preview;
}
