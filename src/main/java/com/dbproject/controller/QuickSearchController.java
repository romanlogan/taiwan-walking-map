package com.dbproject.controller;

import com.dbproject.dto.FastSearchDto;
import com.dbproject.dto.QuickSearchResultDto;
import com.dbproject.service.ExploreService;
import com.dbproject.service.QuickSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class QuickSearchController {


    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final ExploreService exploreService;

    private final QuickSearchService quickSearchService;



    @GetMapping(value = {"/quickSearch","/quickSearch/{page}"})
    public String exploreByQuery(FastSearchDto fastSearchDto,
                                 @PathVariable("page") Optional<Integer> page,
                                 Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        QuickSearchResultDto quickSearchResultDto = quickSearchService.getQuickSearchPage(fastSearchDto,pageable);

//        if( dto = null){
//            model.addAttribute("errorMessage", "");
//            return "errorPage"
//        }

//        model.addAttribute("locationList", locationList);
        model.addAttribute("quickSearchResultDto", quickSearchResultDto);
        model.addAttribute("maxPage", 5);
        model.addAttribute("fastSearchDto", fastSearchDto);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/quickSearch/quickSearchList";
    }

}
