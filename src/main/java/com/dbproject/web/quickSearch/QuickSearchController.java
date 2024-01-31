package com.dbproject.web.quickSearch;

import com.dbproject.api.explore.ExploreService;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.quickSearch.dto.*;
import com.dbproject.api.quickSearch.QuickSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.PushBuilder;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QuickSearchController {


    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreService exploreService;

    private final QuickSearchService quickSearchService;

    private final LocationRepository locationRepository;



    @GetMapping(value = {"/quickSearch","/quickSearch/{page}"})
    public String exploreByQuery(FastSearchDto fastSearchDto,
                                 @PathVariable("page") Optional<Integer> page,
                                 Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        QuickSearchResponse quickSearchResultDto = quickSearchService.getQuickSearchPage(fastSearchDto,pageable);

//        if( dto = null){
//            model.addAttribute("errorMessage", "");
//            return "errorPage"
//        }

//        model.addAttribute("locationList", locationList);
        model.addAttribute("quickSearchResultDto", quickSearchResultDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("fastSearchDto", fastSearchDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/quickSearch/quickSearchPage";
    }

//    이거는 리스트 페이지 처음 들어왔을때 (정렬이나 다른 조건 없음 )
    @GetMapping("/quickSearchList")
    public String exploreQuickSearchList(FastSearchDto fastSearchDto,
                                 Model model) {

        Pageable pageable = PageRequest.of(0, 10);

        QuickSearchListResponse quickSearchListResponse = quickSearchService.getQuickSearchList(fastSearchDto, pageable);

//        model.addAttribute("locationList", locationList);
        model.addAttribute("quickSearchListResponse", quickSearchListResponse);
        model.addAttribute("fastSearchDto", fastSearchDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/quickSearch/quickSearchList";
    }

    @GetMapping("/quickSearch/getTownList")
    public ResponseEntity getTownList(@RequestParam("cityName") String cityName) {

        List<String> townList = locationRepository.findTownListByRegion(cityName);

        return new ResponseEntity(townList, HttpStatus.OK);
    }

    @GetMapping("/quickSearch/newList")
    public String getListByCondition(QuickSearchFormRequest quickSearchFormRequest,
                                             Model model) {

        Pageable pageable = PageRequest.of(0, 10);
        QuickSearchListResponse quickSearchListResponse = quickSearchService.getQuickSearchListByCond(quickSearchFormRequest, pageable);


        model.addAttribute("quickSearchListResponse", quickSearchListResponse);
        model.addAttribute("fastSearchDto", quickSearchFormRequest);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/quickSearch/quickSearchList";
    }




}
