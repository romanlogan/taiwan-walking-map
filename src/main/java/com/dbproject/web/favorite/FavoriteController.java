package com.dbproject.web.favorite;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.DeleteFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.dto.FavoriteLocationListResponse;
import com.dbproject.api.favorite.service.FavoriteService;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.binding.CheckBindingResult;
import com.dbproject.exception.DuplicateFavoriteLocationException;
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

        ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
        if (responseEntity != null) {
            return responseEntity;
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

        Page<FavoriteLocationList> favoriteListResponsePage = favoriteService.getFavoriteLocationList(pageable, email);
        FavoriteLocationListResponse response = new FavoriteLocationListResponse(favoriteListResponsePage);

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

//        if (principal == null) {
//            return new ResponseEntity<String>("로그인 후 이용 해주세요.(server)", HttpStatus.UNAUTHORIZED);
//        }

//        if (bindingResult.hasErrors()) {
//
//            List<String> messageList = new ArrayList<>();
//            List<Object> dataList = new ArrayList<>();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                dataList.add(fieldError.getRejectedValue());
//                messageList.add(fieldError.getDefaultMessage());
//            }
//
////          오류 데이터를 다시 보낼 필요가 없으므로 success 로 유도할 필요 없으므로 BAD_REQUEST
//            return new ResponseEntity(ApiResponse.of(
//                    HttpStatus.BAD_REQUEST,
//                    messageList,
//                    dataList
//            ),HttpStatus.BAD_REQUEST);
//        }

        ResponseEntity responseEntity = CheckBindingResult.induceErrorInAjax(bindingResult);
        if (responseEntity != null) {
            return responseEntity;
        }

        favoriteService.deleteFavoriteLocation(deleteFavoriteLocationRequest.getFavoriteLocationId());

        return new ResponseEntity(1L,HttpStatus.OK);
    }

    @PutMapping("/updateMemo")
    public ResponseEntity updateMemo(@Valid @RequestBody UpdateMemoRequest updateMemoRequest,
                                     BindingResult bindingResult,
                                     Principal principal) {

        ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
        if (responseEntity != null) {
            return responseEntity;
        }

        Long id = favoriteService.updateMemo(updateMemoRequest);

        return new ResponseEntity(id, HttpStatus.OK);
    }

}

















