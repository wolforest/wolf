package cn.coderule.mqclient.core.message;

import cn.coderule.common.util.lang.collection.MapUtil;
import cn.coderule.common.util.lang.string.StringUtil;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Data;

/**
 * com.wolf.mqclient.domain.entity
 *
 * @author Wingle
 * @since 2021/11/30 下午10:26
 **/
@Data
public class Message {
    private static final long serialVersionUID = -2816641304022012152L;
    private String id;
    private String namespace;
    private String group;
    private String topic;
    private final Set<String> tags;

    private byte[] body;

    private Map<String, String> properties;

    private String transactionId;

    private final MessageContext messageContext;

    public String getNewId() {
        return id;
    }

    public Message() {
        tags = new HashSet<>(4);
        properties = new HashMap<>(4);
        messageContext = new MessageContext();
    }

    public String getTag() {
        return tags.iterator().hasNext() ? tags.iterator().next() : null;
    }

    public void addTag(String tag) {
        addTags(tag);
    }

    public void addTags(String... tags) {
        if (tags.length <= 0) {
            return;
        }

        this.tags.addAll(Arrays.asList(tags));
    }

    public void addProperty(String key, String value) {
        if (StringUtil.isBlank(key)) {
            return;
        }

        properties.put(key, value);
    }

    public void addProperties(Map<String, String> args) {
        if (MapUtil.isEmpty(args)) {
            return;
        }

        properties.putAll(args);
    }

    public String getStringBody() {
        return MessageConverter.toString(body);
    }

    public <T> T getJSONObject(Class<T> clazz) {
        return MessageConverter.toJSONObject(body, clazz);
    }

    public boolean isRetried() {
        return messageContext.getReconsumeTimes() > 0;
    }
}
