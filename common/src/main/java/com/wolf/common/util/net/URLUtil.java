package com.wolf.common.util.net;

import lombok.extern.slf4j.Slf4j;
import com.wolf.common.util.collection.MapUtil;
import com.wolf.common.util.lang.CharsetUtil;
import com.wolf.common.util.lang.StringUtil;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * com.wolf.common.util.lang
 *
 * @author Wingle
 * @since 2020/5/7 7:39 下午
 **/
@Slf4j
public class URLUtil {

    public static String getQueryString(String urlString) {
        try {
            URL url = new URL(urlString);
            return url.getQuery();
        } catch (MalformedURLException e) {
            return "";
        }
    }

    public static Map<String, String> parseQuery(String query) {
        try {
            return parseQuery(query, true, CharsetUtil.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            return MapUtil.empty();
        }
    }

    public static Map<String, String> parseQuery(String query, boolean decode, String enc) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<>(8);
        if (StringUtil.isBlank(query)) {
            return result;
        }

        String[] sArr = StringUtil.split(query, "&");
        String[] kvArr;
        String key, value;
        for (String s : sArr) {
            if (StringUtil.isBlank(s)) {
                continue;
            }
            kvArr = StringUtil.split(s, "=");
            if (2 != kvArr.length) {
                continue;
            }

            key = kvArr[0];
            value = kvArr[1];
            if (decode) {
                value = URLDecoder.decode(value, enc);
            }
            result.put(key, value);
        }

        return result;
    }

    public static String encodeQuery(Map<String, Object> paramMap) {
        return encodeQuery(paramMap, false);
    }

    public static String encodeQuery(Map<String, Object> paramMap, boolean ignoreNull) {
        if (MapUtil.isEmpty(paramMap)) {
            return "";
        }

        StringBuilder query = new StringBuilder();
        Object oV, sV;
        boolean isFirst = true;
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
            try {
                oV = entry.getValue();
                if (ignoreNull && null == oV) {
                    continue;
                }

                if (oV != null) {
                    sV = URLEncoder.encode(oV.toString(), StandardCharsets.UTF_8.name());
                } else {
                    sV = "";
                }

                if (!isFirst) {
                    query.append("&");
                }
                isFirst = false;

                query.append(entry.getKey())
                        .append("=")
                        .append(sV);
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            }
        }

        return query.toString();
    }


}
