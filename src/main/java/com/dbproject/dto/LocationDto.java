package com.dbproject.dto;

import com.dbproject.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;


@Getter
@Setter
public class LocationDto {

    private String attractionId;
    private String AttractionName;
    private String Description;
    private BigDecimal PositionLat;
    private BigDecimal PositionLon;

    private static ModelMapper modelMapper = new ModelMapper();

    public static LocationDto of(Location location){
        return modelMapper.map(location,LocationDto.class);
    }

    @Override
    public String toString() {
        return "LocationDto{" +
                "attractionId='" + attractionId + '\'' +
                ", AttractionName='" + AttractionName + '\'' +
                ", Description='" + Description + '\'' +
                ", PositionLat=" + PositionLat +
                ", PositionLon=" + PositionLon +
                '}';
    }
}
