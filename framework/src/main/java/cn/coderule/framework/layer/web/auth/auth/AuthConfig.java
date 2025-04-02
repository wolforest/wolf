package cn.coderule.framework.layer.web.auth.auth;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wolf.auth", ignoreInvalidFields = true)
public class AuthConfig {
    private String accountIdKey = "accountId";
    private String accountKey = "account";
    private String spaceIdKey = "spaceId";
    private String spaceKey = "space";

    private Boolean enable = false;
    private String cookieKey = "WOLF_SESSION_ID";
    private long sessionMaxAge = 60 * 60 * 24 * 100;

    private int cookieMaxAge = 60 * 60 * 24 * 300;
    private String domain;
    private String path = "/";
    private boolean isHttpOnly = true;
    private boolean isSecure = true;

    private List<String> authPath = new ArrayList<>();
    private List<String> excludePath = new ArrayList<>();

    private String denyCode = "NEED_LOGIN";
    private String denyMessage = "Please Login, then retry.";
}
