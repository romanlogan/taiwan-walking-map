package com.dbproject.api.explore;


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
    private List<CityImg> cityImgList = new ArrayList<>();
    private List<LocationDto> locationDtoList = new ArrayList<>();
    private List<RouteDto> routeDtoList = new ArrayList<>();


    public CityDto() {
    }

    public CityDto(String cityName, String cityDetail, List<LocationDto> locationList, List<CityImg> cityImgList) {
        this.cityName = cityName;
        this.cityDetail = cityDetail;
        this.locationDtoList = locationList;
        this.cityImgList = cityImgList;
    }

    @Override
    public String toString() {
        return "CityDto{" +
                "cityName='" + cityName + '\'' +
                ", cityDetail='" + cityDetail + '\'' +
                ", cityImgList=" + cityImgList +
                ", locationList=" + locationDtoList +
                ", routeDtoList=" + routeDtoList +
                '}';
    }


    public void setCityInfo(City city){
        this.cityName = city.getPostalAddressCity();
        this.cityDetail = city.getCityDetail();
        this.positionLat = city.getPositionLat();
        this.positionLon = city.getPositionLon();
    }
}
