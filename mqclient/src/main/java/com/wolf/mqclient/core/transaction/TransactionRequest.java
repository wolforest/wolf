package com.wolf.mqclient.core.transaction;

import com.wolf.mqclient.MQTemplate;
import com.wolf.mqclient.core.message.MessageConverter;
import com.wolf.mqclient.core.message.MessageSendTypeEnum;
import com.wolf.mqclient.core.producer.ProduceCallback;
import com.wolf.mqclient.core.producer.ProduceRequest;
import com.wolf.mqclient.core.producer.ProduceResult;
import com.wolf.mqclient.core.transaction.exception.TransactionListenerNotFoundException;
import java.util.Map;
import lombok.Getter;

/**
 * com.wolf.mqclient.producer
 *
 * @author Wingle
 * @since 2021/12/13 下午6:55
 **/
@Getter
public class TransactionRequest extends ProduceRequest {

    //private Object transactionArg;

    private TransactionRequest(Builder builder) {
        super(builder);

        //transactionArg = builder.transactionArg;
    }

    public static class Builder extends ProduceRequest.Builder {

        //private Object transactionArg;

        public Builder(MQTemplate template) {
            super(template);
            sendType = MessageSendTypeEnum.TRANSACTION;
        }

        public Builder id(String id) {
            super.id(id);
            return this;
        }

        public Builder topic(String topic) {
            super.topic(topic);
            return this;
        }

        public Builder group(String group) {
            super.group(group);
            return this;
        }

        public Builder namespace(String namespace) {
            super.namespace(namespace);
            return this;
        }

        public Builder tag(String tag) {
            return tags(tag);
        }

        public Builder tags(String... tags) {
            super.tags(tags);
            return this;
        }

        public Builder message(String body) {
            super.message(MessageConverter.fromString(body));
            return this;
        }

        public Builder message(Object body) {
            super.message(MessageConverter.fromJSONObject(body));
            return this;
        }

        public Builder message(byte[] body) {
            super.message(body);
            return this;
        }

        public Builder property(String key, String value) {
            super.property(key, value);
            return this;
        }

        public Builder property(Map<String, String> args) {
            super.property(args);
            return this;
        }

        public Transaction begin() {
            TransactionRequest request = new TransactionRequest(this);
            checkTransactionListener(request);
            return template.begin(request);
        }

        public ProduceResult send() {
            //throw new UnsupportedOperationException("Transaction Request send directly not supported");
            TransactionRequest request = new TransactionRequest(this);
            checkTransactionListener(request);
            return template.send(request);
        }

        public void send(ProduceCallback callback) {
            throw new UnsupportedOperationException("Transaction Request send with callback not supported");
        }

        private void checkTransactionListener(TransactionRequest request) {
            if (request.getMessage().getTags().size() > 1) {
                throw new UnsupportedOperationException();
            }
            String topic = request.getMessage().getTopic();
            String tag = request.getMessage().getTags().iterator().next();
            if (null == TransactionCheckerRegistry.get(topic, tag)) {
                throw new TransactionListenerNotFoundException(topic, tag);
            }
        }
    }
}
