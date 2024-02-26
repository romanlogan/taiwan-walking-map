package com.dbproject.web.favorite;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.favorite.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.DeleteFavoriteLocationRequest;
import com.dbproject.api.favorite.FavoriteListResponse;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.api.favorite.FavoriteService;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
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

//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }

        if (bindingResult.hasErrors()) {

            List<String> messageList = new ArrayList<>();
            List<Object> dataList = new ArrayList<>();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                dataList.add(fieldError.getRejectedValue());
                messageList.add(fieldError.getDefaultMessage());
            }

            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    messageList,
                    dataList
            ),HttpStatus.OK);
        }

        String email = principal.getName();
        Long favoriteId;

        try {
            favoriteId = favoriteService.addFavoriteList(addFavoriteLocationRequest, email);
        } catch (DuplicateFavoriteLocationException e) {



//            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity(ApiResponse.of(
                    HttpStatus.BAD_REQUEST,
                    List.of(e.getMessage()),
                    null
            ), HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                null,
                List.of(favoriteId)
        ),  HttpStatus.OK);
    }

    @GetMapping({"/favoriteList","/favoriteList/{page}"})
    public String getFavoriteList(@PathVariable("page") Optional<Integer> page,
                                  Principal principal,
                                  Model model) {

        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5 );
        String email = principal.getName();

        Page<FavoriteListResponse> favoriteListResponsePage = favoriteService.getFavoriteLocationList(pageable, email);
//
        model.addAttribute("locationList", favoriteListResponsePage);
        model.addAttribute("maxPage", 5);
        model.addAttribute("googleMapsApiKey", googleMapsApiKey);

        return "/favorite/favoriteList";
    }

    @DeleteMapping("/deleteFavorite")
    public ResponseEntity deleteFavoriteLocation(@Valid @RequestBody DeleteFavoriteLocationRequest deleteFavoriteLocationRequest,
                                                 Principal principal) {

//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }

        favoriteService.deleteFavoriteLocation(deleteFavoriteLocationRequest.getFavoriteLocationId());

        return new ResponseEntity(1L,HttpStatus.OK);
    }

}

















