package com.wolf.httpclient.util;

import lombok.Getter;
import lombok.NonNull;
import com.wolf.common.util.lang.StringUtil;
import com.wolf.httpclient.HttpRequestBuilder;

public class HttpRequestParser {
    @Getter
    private final HttpRequestBuilder request;
    @Getter
    private String name;

    public HttpRequestParser() {
        request = new HttpRequestBuilder();
    }

    public void parseNameLine(@NonNull String line) {
        String tmp = StringUtil.strip(line, "# ");
        if (StringUtil.notBlank(tmp)) {
            name = tmp;
        } else {
            name = "";
        }
    }

    public void parseLine(String line) {
        if (isBlank(line)) {
            return;
        }
    }

    private boolean isBlank(String line) {
        return StringUtil.isBlank(line);
    }


}
