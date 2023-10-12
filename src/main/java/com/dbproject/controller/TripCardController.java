package com.dbproject.controller;

import com.dbproject.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TripCardController {

    private final TripCardService tripCardService;

    @GetMapping("/tripCard")
    public String test() {

        tripCardService.test();

        return "redirect:/";
    }
}
