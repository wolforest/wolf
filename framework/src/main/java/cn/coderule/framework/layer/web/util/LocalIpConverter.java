package cn.coderule.framework.layer.web.util;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

public class LocalIpConverter extends ClassicConverter {
    private static final String LOCAL_IP = getLocalIp();

    @Override
    public String convert(ILoggingEvent event) {
        return LOCAL_IP;
    }

    private static String getLocalIp() {
        try {
            return IPUtil.getLocalIP();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
