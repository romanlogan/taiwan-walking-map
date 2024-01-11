package com.dbproject.web.favorite;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteFavoriteLocationRequest {

    private String favoriteLocationId;

    public DeleteFavoriteLocationRequest() {
        
    }

    public DeleteFavoriteLocationRequest(String favoriteLocationId) {
        this.favoriteLocationId = favoriteLocationId;
    }
}
