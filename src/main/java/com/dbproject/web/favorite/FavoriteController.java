package com.dbproject.web.favorite;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.DeleteFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.dto.FavoriteLocationListResponse;
import com.dbproject.api.favorite.service.FavoriteService;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.binding.CheckBindingResult;
import com.dbproject.binding.ErrorDetail;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.exception.FavoriteLocationNotExistException;
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

    @PostMapping("/addFavoriteList")
    public ResponseEntity addFavoriteList(
//            requestBody 안쓰면 파라미터 못받아오니 조심하기
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

//            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    null,
                    errorMap
//                    List.of(e.getMessage())
            ), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(favoriteId),
                null
        ),  HttpStatus.OK);
    }

    @GetMapping({"/favoriteList","/favoriteList/{page}"})
    public String getFavoriteList(@PathVariable("page") Optional<Integer> optionalPage,
                                  Principal principal,
                                  Model model) {
        //초과된 page 가 파라미터로 들어오면? 예 page = 1000000000000
        //최대 페이지를 계산 하여 마지막 페이지를 보여준다
        Integer size = 5;
        Integer page = null;
        page = calculatePage(optionalPage, size);

//        Pageable pageable = PageRequest.of(optionalPage.isPresent() ? optionalPage.get() : 0, 5 );
        Pageable pageable = PageRequest.of(page , size );
        String email = principal.getName();

        FavoriteLocationListResponse response = favoriteService.getFavoriteLocationList(pageable, email);

        model.addAttribute("locationList", response);
        model.addAttribute("maxPage", 5);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/favorite/favoriteList";
    }

    private Integer calculatePage(Optional<Integer> optionalPage, Integer size) {
        Integer page;
        Integer maxPage = favoriteService.getMaxPage(size);
        if (optionalPage.isPresent()) {
            if (optionalPage.get() > maxPage) {
                page = maxPage;
            }else{
                page = optionalPage.get();
            }
        }else {
            page = 0;
        }
        return page;
    }

    @DeleteMapping("/deleteFavorite")
    public ResponseEntity deleteFavoriteLocation(@Valid @RequestBody DeleteFavoriteLocationRequest deleteFavoriteLocationRequest,
                                                 BindingResult bindingResult,
                                                 Principal principal) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceErrorInAjax(bindingResult);

            return responseEntity;
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




    @PutMapping("/updateMemo")
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

















