package com.divide;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/status")
@RequiredArgsConstructor
public class StatusController {
    private final ServletWebServerApplicationContext servletWebServerApplicationContext;

    @GetMapping("port")
    public Integer getPort() {
        Integer port = servletWebServerApplicationContext.getWebServer().getPort();
        System.out.println("port = " + port);
        return port;
    }
}
