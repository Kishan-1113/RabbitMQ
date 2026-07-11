package com.example.rabbit_config.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Hello {
    @GetMapping("/hello")
    public String getMethodName() {
        return new String("Hello sir, Welcome !");
    }
}
