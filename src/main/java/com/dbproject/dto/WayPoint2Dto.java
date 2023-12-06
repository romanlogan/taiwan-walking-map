package com.dbproject.dto;

import com.dbproject.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter
@Setter
public class WayPoint2Dto {


    private String AttractionName;
    private String Description;
    private BigDecimal PositionLat;
    private BigDecimal PositionLon;
    private String PostalAddressStreetAddress;
    private String ServiceTimeInfo;
    private String TelephonesTel;
    private String FeeInfo;
    private String WebsiteUrl;
    private String Remarks;
    private String ImagesUrl;

    private static ModelMapper modelMapper = new ModelMapper();

    public static WayPoint2Dto of(Location location){
        return modelMapper.map(location, WayPoint2Dto.class);
    }
}
