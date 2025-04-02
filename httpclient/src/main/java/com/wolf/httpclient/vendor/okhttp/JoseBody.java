package com.wolf.httpclient.vendor.okhttp;

import jakarta.validation.constraints.NotNull;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import okio.ByteString;

import java.io.IOException;

/**
 * com.wolf.httpclient.okhttp
 *
 * @author Wingle
 * @since 2021/8/18 下午10:48
 **/
public class JoseBody extends RequestBody {
    private String body;

    public static JoseBody create(String body) {
        return new JoseBody(body);
    }

    public JoseBody(String body) {
        this.body = body;
    }

    @Override
    public MediaType contentType() {
        return null;
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        if (null == body) {
            body = " ";
        }
        bufferedSink.write(ByteString.encodeUtf8(body));
    }
}
