package com.dbproject.api.favorite.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class DeleteFavoriteLocationRequest {

    @NotNull
    @Min(value = 1, message = "favoriteLocationId不能低於1")
    private Integer favoriteLocationId;

    public DeleteFavoriteLocationRequest() {
        
    }

    public DeleteFavoriteLocationRequest(Integer favoriteLocationId) {
        this.favoriteLocationId = favoriteLocationId;
    }
}
