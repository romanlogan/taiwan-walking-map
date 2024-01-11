package com.dbproject.api.favorite;

import com.dbproject.web.favorite.FavoriteListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;

public interface FavoriteRepositoryCustom {

     Page<FavoriteListResponse> getFavoriteLocationList(Pageable pageable, Principal principal);
}
