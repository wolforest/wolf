package cn.coderule.httpclient.cookie;

import lombok.Data;
import lombok.NonNull;
import cn.coderule.common.util.lang.string.StringUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class Cookie {
    private String name;
    private String value;
    private String domain;
    private String path;
    private LocalDateTime expires;
    private Long maxAge;
    private Boolean secure;
    private Boolean httpOnly;
    private SameSiteEnum sameSite;

    public boolean isValid() {
        if (StringUtil.isBlank(name)) {
            return false;
        }

        if (StringUtil.isBlank(value)) {
            return false;
        }

        return true;
    }

    public boolean domainMatches(@NonNull String requestDomain, String requestPath) {
        if (StringUtil.isBlank(domain)) {
            return false;
        }

        String domainPattern = StringUtil.join("^*.", domain, "$");
        if (!StringUtil.match(requestDomain, domainPattern)) {
            return false;
        }

        if (!requestDomain.equalsIgnoreCase(domain)) {
            return false;
        }

        if (StringUtil.isBlank(path)) {
            return true;
        }

        if (StringUtil.isBlank(requestPath)) {
            return false;
        }

        return StringUtil.startsWith(requestPath, path);
    }

    @Override
    public String toString() {
        if (!isValid()) {
            throw new InvalidCookieException();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value).append("; ");

        if (null != expires) {
            sb.append("Expires=");
            // todolist
            //sb.append(DateUtil.asString(expires, "EEE, dd MMM yyyy HH:mm:ss Z"));
            sb.append(DateTimeFormatter.RFC_1123_DATE_TIME.format(expires));
            sb.append("; ");
        }

        if (null != maxAge) {
            sb.append("Max-Age=").append(maxAge).append("; ");
        }

        if (StringUtil.notBlank(domain)) {
            sb.append("Domain=").append(domain).append("; ");
        }

        if (StringUtil.notBlank(path)) {
            sb.append("Path=").append(path).append("; ");
        }

        if (Boolean.TRUE.equals(secure)) {
            sb.append("Secure").append("; ");
        }

        if (Boolean.TRUE.equals(httpOnly)) {
            sb.append("HttpOnly").append("; ");
        }

        if (null != sameSite) {
            sb.append("SameSite=").append(sameSite.getName()).append("; ");
        }

        return sb.toString();
    }
}
