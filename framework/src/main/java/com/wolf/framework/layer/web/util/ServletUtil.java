package com.wolf.framework.layer.web.util;

import com.wolf.common.util.lang.StringUtil;
import com.wolf.framework.layer.web.HttpEnv;
import jakarta.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

/**
 * com.wolf.framework.util.http
 *
 * @author Wingle
 * @since 2020/11/9 4:31 下午
 **/
@Slf4j
public class ServletUtil {
    public static Map<String, String> getHeaders(HttpServletRequest httpServletRequest) {
        Map<String, String> headerMap = new HashMap<>();

        Enumeration<String> keys = httpServletRequest.getHeaderNames();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            String value = httpServletRequest.getHeader(key);
            headerMap.put(key, value);
        }

        return headerMap;
    }

    public static String getBody(HttpServletRequest httpServletRequest) {
        try {
            BufferedReader br = httpServletRequest.getReader();
            StringBuilder sb = new StringBuilder();
            String str;
            while((str = br.readLine()) != null){
                sb.append(str);
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static void logHeaders(HttpServletRequest httpServletRequest) {
        Enumeration<String> keys = httpServletRequest.getHeaderNames();
        while (keys.hasMoreElements()) {
            StringBuilder sb = new StringBuilder();

            String key = keys.nextElement();
            sb.append(key).append(" : ");

            Enumeration<String> values = httpServletRequest.getHeaders(key);
            while (values.hasMoreElements()) {
                String value = values.nextElement();
                sb.append(value).append("; ");
            }
            log.info(sb.toString());
        }

    }

    public static String getClientIP(HttpServletRequest httpServletRequest) {
        String ip;
        String[] headers = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
        for (String header : headers) {
            ip = httpServletRequest.getHeader(header);
            if (StringUtil.notBlank(ip)) {
                return ip;
            }
        }

        return httpServletRequest.getRemoteAddr();
    }

    public static HttpEnv toTradeEnv(HttpServletRequest httpServletRequest) {
        return HttpEnv.builder()
                .ip(getClientIP(httpServletRequest))
                .build();
    }
}
