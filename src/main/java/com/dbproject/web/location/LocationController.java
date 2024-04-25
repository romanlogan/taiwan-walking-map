package com.dbproject.web.location;


import com.dbproject.api.explore.ExploreService;
import com.dbproject.api.location.dto.LocationDtlResponse;
import com.dbproject.api.location.dto.RecLocationListRequest;
import com.dbproject.api.location.dto.RecommendLocationListResponse;
import com.dbproject.api.location.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LocationController {


    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final LocationService locationService;

    private final ExploreService exploreService;

    @GetMapping("/location/{attractionId}")
    public String locationDtl(@PathVariable("attractionId") String attractionId,
                           Principal principal,
                           Model model) {

        // API 스펙에 의존하게 된 코드(엔티티를 그대로 반환 ) -> Dto를 반환하게 해야함
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
        model.addAttribute("locationDtlResponse", locationDtlResponse);

        return "/location/exploreLocation";
    }




    @GetMapping(value = {"/recommendLocationList","/recommendLocationList/{page}"})
    public String explore(@Valid RecLocationListRequest request,
//            @RequestParam("searchArrival") @NotBlank(message = "도착지는 필수 값 입니다.") String searchArrival,
                          @PathVariable("page") Optional<Integer> page,
                          Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );

        RecommendLocationListResponse response = locationService.getRecommendLocationListResponse(request, pageable);

//        model.addAttribute("locationList", locationList);
//        model.addAttribute("searchConditionDto", searchConditionDto);
        model.addAttribute("response", response);
        model.addAttribute("maxPage", 5);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/location/recommendLocationList";
    }

}
