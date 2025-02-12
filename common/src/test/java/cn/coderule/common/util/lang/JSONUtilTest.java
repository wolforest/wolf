package cn.coderule.common.util.lang;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import cn.coderule.common.util.lang.string.JSONUtil;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/2/26 3:00 下午
 **/
public class JSONUtilTest {

    @Test
    public void getJSONObject() {
        String jsonString = "{  \"entity\":\"event\",  \"account_id\":\"acc_BFQ7uQEaa7j2z7\",  \"event\":\"order.paid\",  \"contains\":[    \"payment\",    \"order\"  ],  \"payload\":{    \"payment\":{      \"entity\":{        \"id\":\"pay_DESlfW9H8K9uqM\",        \"entity\":\"payment\",        \"amount\":100,        \"currency\":\"INR\",        \"status\":\"captured\",        \"order_id\":\"order_DESlLckIVRkHWj\",        \"invoice_id\":null,        \"international\":false,        \"method\":\"netbanking\",        \"amount_refunded\":0,        \"refund_status\":null,        \"captured\":true,        \"description\":null,        \"card_id\":null,        \"bank\":\"HDFC\",        \"wallet\":null,        \"vpa\":null,        \"email\":\"gaurav.kumar@example.com\",        \"contact\":\"+919876543210\",        \"notes\":[],        \"fee\":2,        \"tax\":0,        \"error_code\":null,        \"error_description\":null,        \"created_at\":1567674599      }    },    \"order\":{      \"entity\":{        \"id\":\"order_DESlLckIVRkHWj\",        \"entity\":\"order\",        \"amount\":100,        \"amount_paid\":100,        \"amount_due\":0,        \"currency\":\"INR\",        \"receipt\":\"rcptid #1\",        \"offer_id\":null,        \"status\":\"paid\",        \"attempts\":1,        \"notes\":[],        \"created_at\":1567674581      }    }  },  \"created_at\":1567674606}";

        JSONObject json = JSON.parseObject(jsonString);
        assertNotNull("JsonUtil.getJSONObject fail", json);

        assertEquals("JsonUtil.getJSONObject fail", "order.paid", json.getString("event"));

        JSONObject payment = JSONUtil.getJSONObject(json, "payload.payment.entity");
        assertNotNull("JsonUtil.getJSONObject fail", payment);

        assertEquals("JsonUtil.getJSONObject fail", "captured", payment.getString("status"));

    }

}
