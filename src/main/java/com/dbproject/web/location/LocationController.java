package com.dbproject.web.location;


import com.dbproject.api.explore.ExploreServiceImpl;
import com.dbproject.api.location.dto.*;
import com.dbproject.api.location.service.LocationService;
import com.dbproject.exception.LocationNotExistException;
import com.dbproject.exception.RegionSearchConditionNotValidException;
import com.dbproject.exception.TownSearchConditionNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LocationController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final LocationService locationService;

    private final ExploreServiceImpl exploreService;

    @GetMapping("/location/{attractionId}")
    public String locationDtl(@PathVariable("attractionId") String attractionId,
                           Principal principal,
                           Model model) {

        LocationDtlResponse response ;
        String loggedInUserId;
        try {
            if (principal == null) {
                //로그인 하지 않은 유저
                response = locationService.getLocationDtl(attractionId);
                loggedInUserId = null;
            } else {
                //로그인 한 유저
                response = locationService.getLocationDtlWithAuthUser(attractionId, principal.getName());
                loggedInUserId = principal.getName();
            }
        } catch (LocationNotExistException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", e.getMessage());
            return "main";
        }

        model.addAttribute("loggedInUserId", loggedInUserId);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
        model.addAttribute("locationDtlResponse", response);

        return "location/exploreLocation";
    }

    @GetMapping(value = {"/recommendLocationList","/recommendLocationList/{page}"})
    public String getRecommendLocationList(@Valid RecommendLocationListRequest request,
//            @RequestParam("searchArrival") @NotBlank(message = "도착지는 필수 값 입니다.") String searchArrival,
                          @PathVariable("page") Optional<Integer> page,
                          Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );
        RecommendLocationListResponse response = new RecommendLocationListResponse();

        try {
            response = locationService.getRecommendLocationList(request, pageable);
        } catch (TownSearchConditionNotValidException e) {

            setResponseAndModel(request, model, pageable, response, e);

            return "location/recommendLocationList";

        } catch (RegionSearchConditionNotValidException e){

            request.setSearchArrival("臺北市");
            setResponseAndModel(request, model, pageable, response, e);

            return "location/recommendLocationList";

        }

        addBasicAttributeInModel(model, response);

        return "location/recommendLocationList";
    }

    private void setResponseAndModel(RecommendLocationListRequest request, Model model, Pageable pageable, RecommendLocationListResponse response, RuntimeException e) {
        setResponseWhenExceptionOccur(request, pageable, response);

        e.printStackTrace();
        model.addAttribute("errorMessage", e.getMessage());
        addBasicAttributeInModel(model, response);
    }

    private void setResponseWhenExceptionOccur(RecommendLocationListRequest request, Pageable pageable, RecommendLocationListResponse response) {
        List<String> townList = locationService.getTownListFrom(request.getSearchArrival());
        response.setTownList(townList);
        response.setSearchConditionDto(SearchRequestConditionDto.create(request.getSearchArrival(), request.getSearchQuery(), ""));
        response.setLocationDtoPage(new PageImpl<RecommendLocationDto>(Collections.emptyList(), pageable, 0));
    }

    private void addBasicAttributeInModel(Model model, RecommendLocationListResponse response) {
        model.addAttribute("response", response);
        model.addAttribute("maxPage",  5);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);
    }

}
