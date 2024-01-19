package com.dbproject.api.favorite;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FavoriteRepositoryCustom {

     Page<FavoriteListResponse> getFavoriteLocationListPage(Pageable pageable, String email);
}
