package com.dbproject.api.favorite;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DeleteFavoriteLocationRequest {

    @NotBlank
    private String favoriteLocationId;

    public DeleteFavoriteLocationRequest() {
        
    }

    public DeleteFavoriteLocationRequest(String favoriteLocationId) {
        this.favoriteLocationId = favoriteLocationId;
    }
}
