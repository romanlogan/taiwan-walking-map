package com.dbproject.web.mapSearch;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MapSearchController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;


    @GetMapping(value = "/mapSearch")
    public String exploreByDest(Model model){

        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/map/mapSearch";
    }
}
