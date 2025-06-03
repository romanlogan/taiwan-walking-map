package com.dbproject.api.city.dto;


import com.dbproject.api.city.City;
import com.dbproject.api.city.CityImg;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.route.RouteDto;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class CityDto {

    private String cityName;
    private String cityDetail;
    private BigDecimal positionLat;
    private BigDecimal positionLon;
    private String imgUrl;
    private List<LocationDto> locationDtoList = new ArrayList<>();
    private List<RouteDto> routeDtoList = new ArrayList<>();

    public CityDto() {
    }

    public CityDto(String cityName, String cityDetail, List<LocationDto> locationList) {
        this.cityName = cityName;
        this.cityDetail = cityDetail;
        this.locationDtoList = locationList;
    }

    public void setCityInfo(City city){
        this.cityName = city.getRegion();
        this.cityDetail = city.getCityDetail();
        this.positionLat = city.getPositionLat();
        this.positionLon = city.getPositionLon();
        this.imgUrl = city.getCityImg().getImgUrl();

    }
}
