package com.dbproject.api.quickSearch.dto;

import com.dbproject.api.city.City;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class QuickSearchCityDto {

    private String postalAddressCity;
    private BigDecimal PositionLat;
    private BigDecimal PositionLon;

    public QuickSearchCityDto() {
    }

    @Builder
    public QuickSearchCityDto(String postalAddressCity, BigDecimal positionLat, BigDecimal positionLon) {
        this.postalAddressCity = postalAddressCity;
        this.PositionLat = positionLat;
        this.PositionLon = positionLon;
    }

    public static QuickSearchCityDto of(City city) {
        return QuickSearchCityDto.builder()
                .postalAddressCity(city.getRegion())
                .positionLat(city.getPositionLat())
                .positionLon(city.getPositionLon())
                .build();
    }
}
