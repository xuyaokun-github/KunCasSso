package com.sso.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    public HelloController(){
        System.out.println(this);
    }

    @RequestMapping(value = "/hello")
    public String testLogger() {
        return "success!";
    }


}
