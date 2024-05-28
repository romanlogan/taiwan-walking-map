package com.dbproject.api.favorite.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UpdateMemoRequest {

    @NotNull(message = "favoriteLocationId value is required")
    @Min(value = 1, message = "favoriteLocationId can't be lower than 1")
    private Integer favoriteLocationId;

    private String memo;

    public UpdateMemoRequest() {
    }

    public UpdateMemoRequest(Integer favoriteLocationId, String memo) {
        this.favoriteLocationId = favoriteLocationId;
        this.memo = memo;
    }
}
