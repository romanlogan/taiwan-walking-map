package com.dbproject.web.memo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Setter
@Getter
public class UpdateMemoRequest {

    @NotEmpty
    private String favoriteLocationId;

    private String memo;
}
