package com.dbproject.api.favorite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteLocationListResponse {

    Page<FavoriteLocationList> favoriteListResponsePage;

    public FavoriteLocationListResponse(Page<FavoriteLocationList> favoriteListResponsePage) {
        this.favoriteListResponsePage = favoriteListResponsePage;
    }
}
