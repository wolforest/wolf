package study.daydayup.wolf.business.goods.api.dto.loan;

import study.daydayup.wolf.business.goods.api.vo.Loan;
import study.daydayup.wolf.framework.layer.api.DTO;

import java.math.BigDecimal;

/**
 * study.daydayup.wolf.business.goods.api.dto.loan
 *
 * @author Wingle
 * @since 2020/1/15 10:14 上午
 **/
public class LoanDTO extends Loan implements DTO {
    /**
     * 精度 0.0000%
     */
    private BigDecimal handlingFeeRate;
    private BigDecimal interest;
    private BigDecimal penalty;
}
