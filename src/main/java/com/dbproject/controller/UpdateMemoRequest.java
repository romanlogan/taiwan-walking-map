package com.dbproject.controller;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UpdateMemoRequest {

    private String favoriteLocationId;

    private String memo;
}
