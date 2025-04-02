package com.wolf.common.util.lang.xml;


import org.junit.Test;
import com.wolf.common.util.lang.XMLUtil;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * com.wolf.common.lang.xml
 *
 * @author YIK
 * @since 2022/01/18 上午11:30
 **/
public class XMLParseToObjectTest {

    @Test
    public void happy_path() {
        String happyXML = "<wallet>\n" +
                "<statuscoDe>22</statuscoDe>\n" +
                "<orderid/>\n" +
                "<refid>null</refid>\n" +
                "<amount/>\n" +
                "<statusmessage>No Mid Provided</statusmessage>\n" +
                "<ordertype/>\n" +
                "<checksum>null</checksum>\n" +
                "</wallet>";
        XmlSamplePOJO mobiKwikWallet = XMLUtil.toObject(happyXML, XmlSamplePOJO.class);
        assertNotNull("xml parse to mobiKwikWallet should success", mobiKwikWallet);
    }

    @Test
    public void value_prefix_postfix_with_blank_string() {
        String happyXML = "<wallet>\n" +
                "<statuscode> 22 </statuscode>\n" +
                "<orderid/>\n" +
                "<refid>null</refid>\n" +
                "<amount> 100.01 </amount>\n" +
                "<statusmessage>No Mid Provided</statusmessage>\n" +
                "<ordertype/>\n" +
                "<checksum>null</checksum>\n" +
                "</wallet>";
        XmlSamplePOJO mobiKwikWallet = XMLUtil.toObject(happyXML, XmlSamplePOJO.class);
        assertNotNull("xml parse to mobiKwikWallet should success", mobiKwikWallet);
        assertEquals(" 22 ", mobiKwikWallet.getStatusCode());
        assertEquals(BigDecimal.valueOf(100.01), mobiKwikWallet.getAmount());
    }

    @Test
    public void error_occur_with_bad_format() {
        String happyXML = "<wallet>\n" +
                "<statuscode> 22 <statuscode>\n" +//here bad format
                "<orderid/>\n" +
                "<refid>null</refid>\n" +
                "<amount> 100.01 </amount>\n" +
                "<statusmessage>No Mid Provided</statusmessage>\n" +
                "<ordertype/>\n" +
                "<checksum>null</checksum>\n" +
                "</wallet>";
        XmlSamplePOJO mobiKwikWallet = null;
        try {
            mobiKwikWallet = XMLUtil.toObject(happyXML, XmlSamplePOJO.class);
        } catch (Exception ignored) {

        } finally {
            assertNull("xml parse to mobiKwikWallet should occur error", mobiKwikWallet);
        }


    }

    @Test
    public void error_occur_with_bad_value_datatype() {
        String happyXML = "<wallet>\n" +
                "<statuscode> 22 </statuscode>\n" +
                "<orderid/>\n" +
                "<refid>null</refid>\n" +
                "<amount> A100.01 </amount>\n" +//here bad format
                "<statusmessage>No Mid Provided</statusmessage>\n" +
                "<ordertype/>\n" +
                "<checksum>null</checksum>\n" +
                "</wallet>";
        XmlSamplePOJO mobiKwikWallet = null;
        try {
            mobiKwikWallet = XMLUtil.toObject(happyXML, XmlSamplePOJO.class);
        } catch (Exception e) {

        } finally {
            assertNull("xml parse to mobiKwikWallet should occur error", mobiKwikWallet);
        }

    }

}
