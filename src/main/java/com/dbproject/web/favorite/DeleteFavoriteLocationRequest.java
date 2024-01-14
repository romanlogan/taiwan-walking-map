package com.dbproject.web.favorite;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DeleteFavoriteLocationRequest {

    @NotEmpty
    private String favoriteLocationId;

    public DeleteFavoriteLocationRequest() {
        
    }

    public DeleteFavoriteLocationRequest(String favoriteLocationId) {
        this.favoriteLocationId = favoriteLocationId;
    }
}
