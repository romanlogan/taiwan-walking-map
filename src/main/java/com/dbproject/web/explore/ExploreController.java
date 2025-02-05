package com.dbproject.web.explore;

import com.dbproject.api.city.dto.CityDto;
import com.dbproject.api.explore.ExploreServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ExploreController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreServiceImpl exploreService;

//    @GetMapping("/exploreCity")
//    public @ResponseBody ResponseEntity exploreCity(
//            @RequestParam("searchArrival") String cityName,
//            Model model) {
//
//        if (cityName == "") {
//            return new ResponseEntity<String>("도시 이름이 없습니다.",HttpStatus.BAD_REQUEST);
//        }
//
//        CityDto cityDto = exploreService.getLocationDtl(cityName);
//
//        model.addAttribute("cityDto", cityDto);
//        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
//
//        return new ResponseEntity<Long>(1L, HttpStatus.OK);
//    }

    @GetMapping("/exploreCity")
    public String exploreCity(
            @RequestParam("searchArrival") String cityName,
            Model model) {

        //requestParameter 가 1개일때는 어떻게 spring-validation 을 적용하지 ?
        if (cityName == "") {

            return "error/errorPage";
        }

        CityDto cityDto = exploreService.getLocationDtl(cityName);

        model.addAttribute("cityDto", cityDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "explore/exploreCity";
    }

}
