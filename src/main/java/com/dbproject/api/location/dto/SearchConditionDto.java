package com.dbproject.api.location.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchConditionDto {

    private String arriveCity;

    private String searchQuery;

    private String searchTown;

    public SearchConditionDto() {
    }

    public SearchConditionDto(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    public SearchConditionDto(String arriveCity, String searchQuery, String searchTown) {
        this.arriveCity = arriveCity;
        this.searchQuery = searchQuery;
        this.searchTown = searchTown;
    }
}
