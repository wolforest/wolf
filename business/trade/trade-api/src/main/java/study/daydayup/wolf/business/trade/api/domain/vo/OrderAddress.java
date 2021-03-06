package study.daydayup.wolf.business.trade.api.domain.vo;

import lombok.Data;
import study.daydayup.wolf.business.trade.api.domain.enums.order.AddressTypeEnum;
import study.daydayup.wolf.framework.layer.domain.VO;

/**
 * study.daydayup.wolf.business.trade.api.domain.entity
 *
 * @author Wingle
 * @since 2019/10/5 1:26 PM
 **/
@Data
public class OrderAddress implements VO  {
    /**
     * @see AddressTypeEnum
     */
    private Integer addressType;

    private Integer areaCode;
    private String province;
    private String city;
    private String county;
    private String address;

    private String consignee;
    private String contactInfo;
}
