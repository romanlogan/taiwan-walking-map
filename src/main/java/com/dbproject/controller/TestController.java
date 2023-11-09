package com.dbproject.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {

    @GetMapping("/sample")
    public String sample() {

        return "test/sample";
    }

    @GetMapping("/test1")
    public String test() {

        return "test/test1";
    }
}
