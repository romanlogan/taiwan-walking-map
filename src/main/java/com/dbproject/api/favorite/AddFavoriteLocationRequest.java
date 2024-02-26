package com.dbproject.api.favorite;

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

    @NotNull(message = "locationId值是必要")
    @Length(min = 20, max = 20, message = "locationId要20個字")
    private String locationId;


    @Length(max = 255, message = "memo值只能最多255字")
    private String memo;

    public AddFavoriteLocationRequest() {
    }

    public AddFavoriteLocationRequest(String locationId, String memo) {
        this.locationId = locationId;
        this.memo = memo;
    }


}
