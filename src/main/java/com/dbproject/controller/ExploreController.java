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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ExploreController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreService exploreService;

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
//            @RequestParam("searchArrival") @NotBlank(message = "도시 이름은 필수 값 입니다.") String cityName,
//            BindingResult bindingResult,
            @RequestParam("searchArrival") String cityName,
            Model model) {

        //requestParameter 가 1개일때는 어떻게 spring-validation 을 적용하지 ?

        if (cityName == "") {
            return "/error/errorPage";
//            return new ResponseEntity(HTTP.BAD_REQUEST);

        }

        CityDto cityDto = exploreService.getLocationDtl(cityName);

        model.addAttribute("cityDto", cityDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/explore/exploreCity";
    }

}
