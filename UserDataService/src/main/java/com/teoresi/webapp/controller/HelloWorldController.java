package com.teoresi.webapp.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping(path = "/basicauth")
    public String basicAuthCheck() {
        return "Success";
    }

    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello_World_v2";
    }
}

