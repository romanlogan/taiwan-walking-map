package com.dbproject.api.location;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RecLocationListRequest {


    @NotNull
    private String searchArrival;

    public RecLocationListRequest() {
    }

    public RecLocationListRequest(String searchArrival) {
        this.searchArrival = searchArrival;
    }
}

