package com.dbproject.controller;

import com.dbproject.service.CityService;
import com.dbproject.service.LocationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;
    private final LocationService locationService;


    @GetMapping("/city")
    public String taibei() {

        String cityName = "臺北市";
        return "/city/taibei";
    }
}
