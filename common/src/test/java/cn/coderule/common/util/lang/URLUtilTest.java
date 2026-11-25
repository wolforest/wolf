package cn.coderule.common.util.lang;

import org.junit.jupiter.api.Test;
import cn.coderule.common.util.net.URLUtil;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/5/7 7:53 下午
 **/
public class URLUtilTest {

    @Test
    public void parseUrl() throws UnsupportedEncodingException {
        String url = "processCurrency=INR&amount=100.00&merTransNo=20200507184602600039899850616235&sign=da1b95bbbbeb5979dde19b9ea602eadd11f62ad1a8018a611ae405172f25650c&updateTime=2020-05-07+18%3A46%3A27&pmId=credit.in&transNo=20200507184603457010478245&payerMobile=123456789&createTime=2020-05-07+18%3A46%3A03&processAmount=100.00&payerName=alakd&currency=INR&payerEmail=sklds%40gmail.com&transStatus=success";
        Map<String, String> data = URLUtil.parseQuery(url);


        assertNotNull(data, "UrlUtil.parseUrl parse fail");
        assertEquals(data.size(), 14, "UrlUtil.parseUrl parse fail");
        assertEquals(data.get("merTransNo"), "20200507184602600039899850616235", "UrlUtil.parseUrl parse fail");
        assertEquals(data.get("payerEmail"), "sklds@gmail.com", "UrlUtil.parseUrl parse fail");

    }
}
