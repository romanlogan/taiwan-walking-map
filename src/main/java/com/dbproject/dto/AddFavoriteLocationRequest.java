package com.dbproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFavoriteLocationRequest {

    private String locationId;

    public AddFavoriteLocationRequest() {
    }

    public AddFavoriteLocationRequest(String locationId) {
        this.locationId = locationId;
    }
}
