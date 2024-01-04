package com.dbproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFavoriteLocationRequest {

    private String locationId;

    private String memo;

    public AddFavoriteLocationRequest() {
    }

    public AddFavoriteLocationRequest(String locationId, String memo) {
        this.locationId = locationId;
        this.memo = memo;
    }
}
