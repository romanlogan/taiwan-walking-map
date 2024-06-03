package com.dbproject.api.location.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequestConditionDto {

    private String arriveCity;

    private String searchQuery;

    private String searchTown;

    public SearchRequestConditionDto() {
    }

    public SearchRequestConditionDto(String arriveCity) {
        this.arriveCity = arriveCity;
    }

    @Builder
    public SearchRequestConditionDto(String arriveCity, String searchQuery, String searchTown) {
        this.arriveCity = arriveCity;
        this.searchQuery = searchQuery;
        this.searchTown = searchTown;
    }

    public static SearchRequestConditionDto create(String arriveCity, String searchQuery, String searchTown) {

        return SearchRequestConditionDto.builder()
                .arriveCity(arriveCity)
                .searchQuery(searchQuery)
                .searchTown(searchTown)
                .build();
    }
}
