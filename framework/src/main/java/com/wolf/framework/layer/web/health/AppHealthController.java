package com.wolf.framework.layer.web.health;

import com.wolf.framework.layer.web.Controller;
import com.wolf.framework.layer.web.util.IPUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weixing
 * @since 2023/10/12 15:36
 */
@Slf4j
@RestController
public class AppHealthController implements Controller {

    private volatile AppHealthStatusEnum status = AppHealthStatusEnum.DOWN;

    @GetMapping(value = "/wolf/health")
    public ResponseEntity<String> status() {
        return new ResponseEntity<>(status.name(), HttpStatus.valueOf(status.getCode()));
    }

    @GetMapping(value = "/wolf/health/up")
    public synchronized ResponseEntity<String> up(HttpServletRequest request) {
        String fromIp = IPUtil.getIP(request);
        log.info("try to set app up. from ip: {}", fromIp);

        if (!IPUtil.isLocalIP(fromIp)) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        } else {
            status = AppHealthStatusEnum.UP;
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
    }

    @GetMapping(value = "/wolf/health/down")
    public synchronized ResponseEntity<String> down(HttpServletRequest request) {
        String fromIp = IPUtil.getIP(request);
        log.info("try to set app down. from ip: {}", fromIp);

        if (!IPUtil.isLocalIP(fromIp)) {
            return new ResponseEntity<>("", HttpStatus.NOT_FOUND);
        } else {
            status = AppHealthStatusEnum.DOWN;
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
    }
}
