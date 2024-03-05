package com.dbproject.api.favorite.service;

import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteService {

    Long addFavoriteList(AddFavoriteLocationRequest addFavoriteLocationRequest, String email) throws DuplicateFavoriteLocationException;
//    void checkDuplicateFavoriteLocation(String locationId,String email);
    Page<FavoriteLocationList> getFavoriteLocationList(Pageable pageable, String email);
    void deleteFavoriteLocation(Integer favoriteLocationId);
    Integer getMaxPage(Integer size);
    Long updateMemo(UpdateMemoRequest updateMemoRequest);
}
