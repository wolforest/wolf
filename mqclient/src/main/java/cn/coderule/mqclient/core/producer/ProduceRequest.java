package cn.coderule.mqclient.core.producer;

import cn.coderule.mqclient.core.message.MessageSendTypeEnum;
import cn.coderule.mqclient.MQTemplate;
import cn.coderule.mqclient.core.message.Message;
import cn.coderule.mqclient.core.message.MessageConverter;
import cn.coderule.mqclient.core.message.MessageSendModeEnum;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.Data;
import lombok.NonNull;

/**
 * com.wolf.mqclient.producer
 *
 * @author Wingle
 * @since 2021/11/30 下午10:25
 **/
//@Getter
@Data
public class ProduceRequest {
    protected Message message;
    protected MessageSendTypeEnum sendType;
    protected MessageSendModeEnum sendMode;

    protected Long delay;
    protected LocalDateTime activeAt;

    protected Object orderBy;

    protected ProduceRequest(Builder builder) {
        message = builder.message;
        sendType = builder.sendType;
        sendMode = builder.sendMode;
        delay = builder.delay;
        activeAt = builder.activeAt;
        orderBy = builder.orderBy;
    }

    public static class Builder {
        protected final MQTemplate template;

        protected Message message;
        protected MessageSendTypeEnum sendType;
        protected MessageSendModeEnum sendMode;

        protected Long delay;
        protected LocalDateTime activeAt;

        protected Object orderBy;

        public Builder(MQTemplate template) {
            this.template = template;
            this.message = new Message();
        }

        public Builder id(String id) {
            message.setId(id);
            return this;
        }

        public Builder topic(String topic) {
            message.setTopic(topic);
            return this;
        }

        public Builder group(String group) {
            message.setGroup(group);
            return this;
        }

        public Builder namespace(String namespace) {
            message.setNamespace(namespace);
            return this;
        }

        public Builder tag(String tag) {
            return tags(tag);
        }

        public Builder tags(String... tags) {
            message.addTags(tags);
            return this;
        }

        public Builder message(String body) {
            message.setBody(MessageConverter.fromString(body));
            return this;
        }

        public Builder message(Object body) {
            message.setBody(MessageConverter.fromJSONObject(body));
            return this;
        }

        public Builder message(byte[] body) {
            message.setBody(body);
            return this;
        }

        public Builder property(String key, String value) {
            message.addProperty(key, value);
            return this;
        }

        public Builder property(Map<String, String> args) {
            message.addProperties(args);
            return this;
        }

        public Builder at(@NonNull LocalDateTime time) {
            sendType = MessageSendTypeEnum.SCHEDULE;
            activeAt = time;
            return this;
        }

        public Builder delay(int s) {
            return delay((long) s);
        }

        public Builder delay(long s) {
            long ms = s * 1000;
            sendType = MessageSendTypeEnum.SCHEDULE;
            delay = ms;
            return this;
        }

        public Builder orderBy(Object arg) {
            sendType = MessageSendTypeEnum.ORDER;
            orderBy = arg;
            return this;
        }

        public Builder oneWay() {
            sendType = MessageSendTypeEnum.ONE_WAY;
            return this;
        }

        public ProduceResult send() {
            ProduceRequest request = new ProduceRequest(this);
            return template.send(request);
        }

        public void send(ProduceCallback callback) {
            ProduceRequest request = new ProduceRequest(this);
            template.send(request, callback);
        }
    }
}
