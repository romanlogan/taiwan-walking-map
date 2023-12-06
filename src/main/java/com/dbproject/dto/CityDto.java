package com.dbproject.dto;

import com.dbproject.entity.CityImg;
import com.dbproject.entity.Location;
import com.dbproject.entity.Route;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class CityDto {

    private String cityName;
    private String cityDetail;
    private BigDecimal PositionLat;
    private BigDecimal PositionLon;
    private List<CityImg> cityImgList = new ArrayList<>();
    private List<Location> locationList = new ArrayList<>();

    private List<RouteDto> routeDtoList = new ArrayList<>();


    public CityDto() {
    }

    public CityDto(String cityName, String cityDetail, List<Location> locationList, List<CityImg> cityImgList) {
        this.cityName = cityName;
        this.cityDetail = cityDetail;
        this.locationList = locationList;
        this.cityImgList = cityImgList;
    }

    @Override
    public String toString() {
        return "CityDto{" +
                "cityName='" + cityName + '\'' +
                ", cityDetail='" + cityDetail + '\'' +
                ", cityImgList=" + cityImgList +
                ", locationList=" + locationList +
                ", routeDtoList=" + routeDtoList +
                '}';
    }
}
