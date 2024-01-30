package com.dbproject.api.quickSearch.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FastSearchDto {


    private String searchQuery;
    private String searchCity;

    @Builder
    public FastSearchDto(String searchQuery, String searchCity) {
        this.searchQuery = searchQuery;
        this.searchCity = searchCity;
    }

    public static FastSearchDto create(String searchQuery, String searchCity) {
        return FastSearchDto.builder()
                .searchQuery(searchQuery)
                .searchCity(searchCity)
                .build();
    }
}
