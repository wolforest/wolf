package cn.coderule.common.util.lang.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

import java.math.BigDecimal;

/**
 * for junit test
 */
@Data
@JacksonXmlRootElement(localName = "wallet")
public class XmlSamplePOJO {
    @JacksonXmlProperty(localName = "statuscode")
    private String statusCode;

    @JacksonXmlProperty(localName = "orderid")
    private String orderId;

    private String mid;

    @JacksonXmlProperty(localName = "refid")
    private String refId;

    @JacksonXmlProperty(localName = "amount")
    private BigDecimal amount;

    @JacksonXmlProperty(localName = "statusmessage")
    private String statusMessage;

    @JacksonXmlProperty(localName = "ordertype")
    private String orderType;

    @JacksonXmlProperty(localName = "checksum")
    private String checksum;

}
