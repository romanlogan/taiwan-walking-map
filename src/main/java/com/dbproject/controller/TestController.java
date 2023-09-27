package com.dbproject.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TestController {


    @GetMapping("/test1")
    public String test() {

        return "test/test1";
    }
}
