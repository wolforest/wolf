package com.wolf.framework.layer.web;

import com.wolf.framework.layer.domain.VO;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * com.wolf.business.trade.api.domain.vo.buy
 *
 * @author Wingle
 * @since 2019/12/13 3:28 下午
 **/
@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpEnv implements VO  {
    private String deviceId;
    private String imei;

    private String attributionChannel;
    private String channelId;

    private String androidId;
    private String idfa;
    private String idfv;

    private String ip;
    private BigDecimal latitude;
    private BigDecimal longitude;

    private String adid;
    private String gpsAdid;
    private String fireAdid;


}
