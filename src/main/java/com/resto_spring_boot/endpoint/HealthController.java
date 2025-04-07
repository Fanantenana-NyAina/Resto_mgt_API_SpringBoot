package com.resto_spring_boot.endpoint;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    @GetMapping("/ping")
    public String HealthRestController() {
        return "pong";
    }
}
