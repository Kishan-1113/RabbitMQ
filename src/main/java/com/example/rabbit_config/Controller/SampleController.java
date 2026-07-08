package com.example.rabbit_config.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/sample")
public class SampleController {

    @GetMapping("/get")
    public String getMethodName(@RequestParam String param) {
        return new String("Sample controller respondinig");
    }

    @PostMapping("/post")
    public String postMethodName(@RequestBody String entity) {
        return entity;
    }

}
