package com.dbproject.web.favorite;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.DeleteFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.FavoriteLocationListResponse;
import com.dbproject.api.favorite.service.FavoriteService;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.binding.CheckBindingResult;
import com.dbproject.binding.ErrorDetail;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.exception.FavoriteLocationNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/favorite")
public class FavoriteController {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity addFavoriteList(
            @Valid @RequestBody AddFavoriteLocationRequest addFavoriteLocationRequest,
            BindingResult bindingResult,
            Principal principal) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        String email = principal.getName();
        Long favoriteId;

        try {
            favoriteId = favoriteService.addFavoriteList(addFavoriteLocationRequest, email);
        } catch (DuplicateFavoriteLocationException e) {

            Map<String, ErrorDetail> errorMap = new HashMap<>();
            ErrorDetail errorDetail = new ErrorDetail(null,e.getMessage());
            errorMap.put("DuplicateFavoriteLocationException", errorDetail);

            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    null,
                    errorMap
            ), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(favoriteId),
                null
        ),  HttpStatus.OK);
    }

    @GetMapping({"/favoriteList","/favoriteList/{page}"})
    public String getFavoriteLocationList(@PathVariable("page") Optional<Integer> enteredPageParameter,
                                          Principal principal,
                                          Model model) {

        //If the excess page is entered as a parameter (for example page = 100000)
        //Calculate the maximum page and show the last page
        String email = principal.getName();
        Integer pageSize = 5;
        Integer page = null;
        page = calculatePage(enteredPageParameter, pageSize, email);

        Pageable pageable = PageRequest.of(page , pageSize);

        FavoriteLocationListResponse response = favoriteService.getFavoriteLocationList(pageable, email);

        model.addAttribute("locationList", response);
        model.addAttribute("maxPage", 5);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "favorite/favoriteList";
    }

//
    private Integer calculatePage(Optional<Integer> enteredPageParameter, Integer pageSize, String email) {
        Integer page;
        Integer maxPage = favoriteService.getMaxPage(pageSize, email);      // calculate maximum page of user favorite list
        if (enteredPageParameter.isPresent()) {
            if (enteredPageParameter.get() > maxPage) {
                // if parameter that received from user is bigger than maximum page of user favorite list,
                // return maximum page of user favorite list
                page = maxPage;
            }else{
                page = enteredPageParameter.get();
            }
        }else {
            page = 0;
        }
        return page;
    }

    @DeleteMapping
    public ResponseEntity deleteFavoriteLocation(@Valid @RequestBody DeleteFavoriteLocationRequest deleteFavoriteLocationRequest,
                                                 BindingResult bindingResult) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceErrorInAjax(bindingResult);
        }

        try {
            favoriteService.deleteFavoriteLocation(deleteFavoriteLocationRequest);
        } catch (FavoriteLocationNotExistException e) {
            return null;
        }

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(1L),
                null
        ),  HttpStatus.OK);
    }


    @PutMapping("/memo")
    public ResponseEntity updateMemo(@Valid @RequestBody UpdateMemoRequest updateMemoRequest,
                                     BindingResult bindingResult,
                                     Principal principal) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        Long id;

        try {
            id = favoriteService.updateMemo(updateMemoRequest);
        } catch (FavoriteLocationNotExistException e) {
            return null;
        }

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(id),
                null
        ),  HttpStatus.OK);
    }

}

















