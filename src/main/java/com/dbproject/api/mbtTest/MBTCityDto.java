package com.dbproject.api.mbtTest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MBTCityDto {

    private String postal_address_city;

    private String city_detail;

    private BigDecimal position_lat;

    private BigDecimal position_lon;

    public MBTCityDto(String postal_address_city, String city_detail, BigDecimal position_lat, BigDecimal position_lon) {
        this.postal_address_city = postal_address_city;
        this.city_detail = city_detail;
        this.position_lat = position_lat;
        this.position_lon = position_lon;
    }

    public MBTCityDto() {
    }

    public void printDetail() {
        System.out.println("mbtTest : " + postal_address_city + ", " + city_detail+", " + position_lat + ", " + position_lon);
    }
}
