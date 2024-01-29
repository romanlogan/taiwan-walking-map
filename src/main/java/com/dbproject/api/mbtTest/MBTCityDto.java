package com.dbproject.api.mbtTest;

import lombok.Data;

@Data
public class MBTCityDto {

    private String postalAddressCity;

    private String cityDetail;

    public MBTCityDto(String postalAddressCity, String cityDetail) {
        this.postalAddressCity = postalAddressCity;
        this.cityDetail = cityDetail;
    }

    public MBTCityDto() {

    }
}
