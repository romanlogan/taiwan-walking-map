package com.dbproject.controller;

import com.dbproject.dto.LocationListDto;
import com.dbproject.dto.SearchByCityDto;
import com.dbproject.entity.Location;
import com.dbproject.service.ExploreService;
import com.dbproject.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ExploreController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreService exploreService;

    @GetMapping(value = {"/explore","/explore/{page}"})
    public String explore(@RequestParam("searchArrival") String searchArrival,
//                            @RequestParam("searchDepartDate") String searchDepartDate,
//                           @RequestParam("searchArrivalDate") String searchArrivalDate,
                           @PathVariable("page") Optional<Integer> page,
                           Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 10);

//        LocalDate departDate = LocalDate.parse(searchDepartDate);
//        LocalDate arrivalDate = LocalDate.parse(searchArrivalDate);
//        SearchByCityDto searchByCityDto = new SearchByCityDto(searchArrival, departDate, arrivalDate);
        SearchByCityDto searchByCityDto = new SearchByCityDto(searchArrival);

        Page<Location> locationList = exploreService.getLocationPageByCity(searchByCityDto,pageable);
        model.addAttribute("locationList", locationList);
        model.addAttribute("maxPage", 5);
        model.addAttribute("searchByCityDto", searchByCityDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/explore";
    }

    @GetMapping(value = "/exploreByDest")
    public String exploreByDest(
//            @RequestParam("searchDepart") String origin,
//                                 @RequestParam("searchArrival") String destination,
                                 Model model){

//        System.out.println("origin = "+origin);
//        System.out.println("destination = "+destination);
//
//        model.addAttribute("origin", origin);
//        model.addAttribute("destination", destination);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/exploreDest";
    }

    @GetMapping("/waypointTest")
    public String waypointTest(Model model){
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/waypointTest";
    }
}
