package com.dbproject.web.memo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class UpdateMemoRequest {

    @NotBlank
    private String favoriteLocationId;

    private String memo;

    public UpdateMemoRequest() {
    }

    public UpdateMemoRequest(String favoriteLocationId, String memo) {
        this.favoriteLocationId = favoriteLocationId;
        this.memo = memo;
    }
}
