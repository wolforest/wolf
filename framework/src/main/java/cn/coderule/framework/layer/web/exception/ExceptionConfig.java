package cn.coderule.framework.layer.web.exception;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "wolf.exception")
public class ExceptionConfig {
    private boolean enableRestHandler = false;
}
