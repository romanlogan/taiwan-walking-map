package com.dbproject.controller;

import com.dbproject.dto.SearchByCityDto;
import com.dbproject.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final LocationService locationService;

    @GetMapping("/")
    public String main(Model model) {

        locationService.locationInfo();
        model.addAttribute("searchByCityDto", new SearchByCityDto());

        return "main";
    }

}
