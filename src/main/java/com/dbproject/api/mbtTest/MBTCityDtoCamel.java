package com.dbproject.api.mbtTest;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MBTCityDtoCamel {


    private String postalAddressCity;

    private String cityDetail;

    private BigDecimal positionLat;

    private BigDecimal positionLon;

    public MBTCityDtoCamel() {
    }

    public MBTCityDtoCamel(String postalAddressCity, String cityDetail, BigDecimal positionLat, BigDecimal positionLon) {
        this.postalAddressCity = postalAddressCity;
        this.cityDetail = cityDetail;
        this.positionLat = positionLat;
        this.positionLon = positionLon;
    }

    public void printDetail() {
        System.out.println("mbtTest : " + postalAddressCity + ", " + cityDetail+", " + positionLat + ", " + positionLon);
    }
}
