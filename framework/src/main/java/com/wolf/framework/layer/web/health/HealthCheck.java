package com.wolf.framework.layer.web.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * HealthCheck
 *
 * @author: YIK
 * @since: 2022/3/9 6:21 PM
 */
@RestController
public class HealthCheck {

    @GetMapping("/status")
    public String status() {
        return "OK";
    }
}
