package com.dbproject.controller;


import com.dbproject.dto.LocationDtlResponse;
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
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreService exploreService;

    @GetMapping("/location/{attractionId}")
    public String locationDtl(@PathVariable("attractionId") String attractionId,
                           Principal principal,
                           Model model) {

        // API 스펙에 의존하게 된 코드
//        Location location = locationService.getLocationDtl(attractionId);

        LocationDtlResponse locationDtlResponse;

        if (principal == null) {

            //로그인 하지 않은 유저
            locationDtlResponse = locationService.getLocationDtl(attractionId);
        }else{
            //로그인 한 유저
            locationDtlResponse = locationService.getLocationDtlWithAuthUser(attractionId,principal.getName());
        }


        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        model.addAttribute("location", locationDtlResponse);

        return "/location/exploreLocation";
    }




    @GetMapping(value = {"/recommendLocationList","/recommendLocationList/{page}"})
    public String explore(@RequestParam("searchArrival") @NotBlank(message = "도착지는 필수 값 입니다.") String searchArrival,
                          @PathVariable("page") Optional<Integer> page,
                          Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );

        SearchByCityDto searchByCityDto = new SearchByCityDto(searchArrival);

        //리포지토리에서 dto를 바로 반환하도록 해서 dto 로 page<Location> 을 감싸야 하나 ?
        Page<Location> locationList = locationService.getLocationPageByCity(searchByCityDto, pageable);

        model.addAttribute("locationList", locationList);
        model.addAttribute("maxPage", 5);
        model.addAttribute("searchByCityDto", searchByCityDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/location/recommendLocationList";
    }

}
