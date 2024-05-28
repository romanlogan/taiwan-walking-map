package com.dbproject.api.favorite.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddFavoriteLocationRequest {

    @NotNull(message = "locationId value is required")
    @Length(min = 20, max = 20, message = "locationId requires only 20 characters")
    private String locationId;


    @Length(max = 255, message = "The memo value can be up to 255 characters long.")
    private String memo;

    public AddFavoriteLocationRequest() {
    }

    @Builder
    public AddFavoriteLocationRequest(String locationId, String memo) {
        this.locationId = locationId;
        this.memo = memo;
    }

    public static AddFavoriteLocationRequest create(String locationId, String memo) {

        return AddFavoriteLocationRequest.builder()
                .locationId(locationId)
                .memo(memo)
                .build();
    }


}
