package com.dbproject.dto;

import com.dbproject.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;

@Getter
@Setter
public class ArrivePointDto {

    private String name;
    private String tolDescribe;
    private String longitude;
    private String latitude;
    private String address;
    private String openTime;
    private String tel;
    private String ticketInfo;
    private String website;
    private String remarks;
    private String picture1;


    private static ModelMapper modelMapper = new ModelMapper();

    public static ArrivePointDto of(Location location){
        return modelMapper.map(location, ArrivePointDto.class);
    }
}
