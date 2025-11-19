package com.back;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloController {
    @GetMapping("/")
    @ResponseBody
    public  String admin() {
        return "Hello Admin!";
    }

    @GetMapping("/hello")
    @ResponseBody
    public  String hello() {
        return "Hello World!";
    }
}