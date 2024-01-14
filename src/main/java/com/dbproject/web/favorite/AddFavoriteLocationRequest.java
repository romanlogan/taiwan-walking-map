package com.dbproject.web.favorite;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class AddFavoriteLocationRequest {

    @NotEmpty
    private String locationId;

    private String memo;

    public AddFavoriteLocationRequest() {
    }

    public AddFavoriteLocationRequest(String locationId, String memo) {
        this.locationId = locationId;
        this.memo = memo;
    }


}
