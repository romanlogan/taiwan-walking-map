package com.dbproject.controller;

import com.dbproject.dto.CityDto;
import com.dbproject.dto.FastSearchDto;
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

    @GetMapping("/exploreCity")
    public String exploreCity(@RequestParam("searchArrival") String cityName,
                            Model model) {

        CityDto cityDto = exploreService.getLocationDtl(cityName);

        model.addAttribute("cityDto", cityDto);
        return "/explore/exploreCity";
    }




    @GetMapping(value = "/exploreByDest")
    public String exploreByDest(Model model){

        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/exploreDest";
    }

    @GetMapping(value = {"/explore","/explore/{page}"})
    public String explore(@RequestParam("searchArrival") String searchArrival,
                          @PathVariable("page") Optional<Integer> page,
                          Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );

        SearchByCityDto searchByCityDto = new SearchByCityDto(searchArrival);

        Page<Location> locationList = exploreService.getLocationPageByCity(searchByCityDto, pageable);

        model.addAttribute("locationList", locationList);
        model.addAttribute("maxPage", 5);
        model.addAttribute("searchByCityDto", searchByCityDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/explore";
    }

    @GetMapping(value = {"/exploreByQuery","/exploreByQuery/{page}"})
    public String exploreByQuery(FastSearchDto fastSearchDto,
                                 @PathVariable("page") Optional<Integer> page,
                                 Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<Location> locationList = exploreService.getLocationListBySearchQuery(fastSearchDto,pageable);

//        if( dto = null){
//            model.addAttribute("errorMessage", "");
//            return "errorPage"
//        }

        if (locationList.getTotalElements() == 0) {

        }
        System.out.println("********************************************");
        System.out.println(locationList.getTotalElements());
        System.out.println("********************************************");




        model.addAttribute("locationList", locationList);
        model.addAttribute("maxPage", 5);
        model.addAttribute("fastSearchDto", fastSearchDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/exploreByQuery";
    }


}
