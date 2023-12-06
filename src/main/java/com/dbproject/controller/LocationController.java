package com.dbproject.controller;


import com.dbproject.entity.Location;
import com.dbproject.service.LocationService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @GetMapping("/location/{attractionId}")
    public String location(@PathVariable("attractionId") String attractionId,
                           Model model) {


        Location location = locationService.getLocationDtl(attractionId);

        model.addAttribute("location", location);

        return "/explore/exploreLocation";
    }



}
