package com.dbproject.controller;

import com.dbproject.entity.TripCard;
import com.dbproject.service.TripCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class TripCardController {

    private final TripCardService tripCardService;

    @GetMapping("/exploreTripCard")
    public String exploreTripCard(@RequestParam("tripCardCity") String tripCardCity,
                                  Model model) {

        List<TripCard> tripCardList = tripCardService.getTripCardAddress(tripCardCity);
        model.addAttribute("tripCardList", tripCardList);

        return "/explore/exploreTripCard";
    }
}
