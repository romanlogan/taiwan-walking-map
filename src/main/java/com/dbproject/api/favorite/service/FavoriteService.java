package com.dbproject.api.favorite.service;

import com.dbproject.api.favorite.dto.*;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {

    Long addFavoriteList(AddFavoriteLocationRequest addFavoriteLocationRequest, String email) throws DuplicateFavoriteLocationException;
//    void checkDuplicateFavoriteLocation(String locationId,String email);
    FavoriteLocationListResponse getFavoriteLocationList(Pageable pageable, String email);
    void deleteFavoriteLocation(DeleteFavoriteLocationRequest request);
    Integer getMaxPage(Integer size);
    Long updateMemo(UpdateMemoRequest updateMemoRequest);
}
