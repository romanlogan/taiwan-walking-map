package com.dbproject.web.memo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateMemoRequest {

    private String favoriteLocationId;

    private String memo;
}
