package com.dbproject.controller;


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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreService exploreService;

    @GetMapping("/location/{attractionId}")
    public String location(@PathVariable("attractionId") String attractionId,
                           Model model) {


        Location location = locationService.getLocationDtl(attractionId);

        model.addAttribute("location", location);

        return "/location/exploreLocation";
    }




    @GetMapping(value = {"/recommendLocationList","/recommendLocationList/{page}"})
    public String explore(@RequestParam("searchArrival") @NotBlank(message = "도착지는 필수 값 입니다.") String searchArrival,
                          @PathVariable("page") Optional<Integer> page,
                          Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );

        SearchByCityDto searchByCityDto = new SearchByCityDto(searchArrival);

        Page<Location> locationList = locationService.getLocationPageByCity(searchByCityDto, pageable);

        model.addAttribute("locationList", locationList);
        model.addAttribute("maxPage", 5);
        model.addAttribute("searchByCityDto", searchByCityDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/location/recommendLocationList";
    }

}
