package com.dbproject.api.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RecommendLocationListRequest {

    @NotBlank(message = "Arrival city name is required.")
    private String searchArrival;

    //없으면 모든 검색 결과 반환, 최대 10자 까지
    @NotNull(message = "searchQuery cannot be null")        //bindException 이 안나옴 ?
//    @Max(value = 10,message = "searchQuery can only be up to 10 characters long.")
    private String searchQuery;

    //없으면 모든 검색 결과 반환, 최대 4자 까지
    @NotNull(message = "searchTown cannot be null")
//    @Max(value = 4,message = "searchTown can only be up to 4 characters long.")
    private String searchTown;

    public RecommendLocationListRequest() {
    }

    @Builder
    public RecommendLocationListRequest(String searchArrival, String searchQuery, String searchTown) {
        this.searchArrival = searchArrival;
        this.searchQuery = searchQuery;
        this.searchTown = searchTown;
    }


}

