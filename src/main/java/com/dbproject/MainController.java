package com.dbproject;

import com.dbproject.api.location.service.LocationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final LocationServiceImpl locationService;

    @GetMapping("/")
    public String main(Model model) {

        return "main";
    }


}
