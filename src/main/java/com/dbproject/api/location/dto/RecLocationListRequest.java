package com.dbproject.api.location.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RecLocationListRequest {


    @NotNull
    private String searchArrival;

    private String searchQuery;

    private String searchTown;

    public RecLocationListRequest() {
    }

    public RecLocationListRequest(String searchArrival) {
        this.searchArrival = searchArrival;
    }
}

