package com.dbproject.web.route;


import com.dbproject.api.location.Location;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class WayPoint1Dto {

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

    public static WayPoint1Dto of(Location location){
        return modelMapper.map(location, WayPoint1Dto.class);
    }

}
