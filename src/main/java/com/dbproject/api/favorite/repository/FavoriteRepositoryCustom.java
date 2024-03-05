package com.dbproject.api.favorite.repository;

import com.dbproject.api.favorite.dto.FavoriteLocationList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRepositoryCustom {

     Page<FavoriteLocationList> getFavoriteLocationListPage(Pageable pageable, String email);
}
