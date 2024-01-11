package com.dbproject.web.location;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecLocationListRequest {

    private String searchArrival;

    public RecLocationListRequest() {
    }

    public RecLocationListRequest(String searchArrival) {
        this.searchArrival = searchArrival;
    }
}

