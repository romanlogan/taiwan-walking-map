package com.dbproject.api.location.dto;


import com.dbproject.api.location.Location;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class LocationDto {

    private String locationId;
    private String name;
    private Integer commentCount;
    private String tolDescribe;
    private String description;
    private String tel;
    private String address;
    private String region;
    private String town;
    private String travellingInfo;
    private String openTime;
    private String longitude;
    private String latitude;
    private String website;
    private String parkingInfo;
    private String ticketInfo;
    private String remarks;

    private String picture1;

    private static ModelMapper mapper = new ModelMapper();

    public static LocationDto of(Location location) {

        return mapper.map(location, LocationDto.class);
    }

    public LocationDto() {
    }

    @QueryProjection
    public LocationDto(String locationId, String name, Integer commentCount, String tolDescribe, String description, String tel, String address, String region, String town, String travellingInfo, String openTime, String longitude, String latitude, String website, String parkingInfo, String ticketInfo, String remarks, String picture1) {
        this.locationId = locationId;
        this.name = name;
        this.commentCount = commentCount;
        this.tolDescribe = tolDescribe;
        this.description = description;
        this.tel = tel;
        this.address = address;
        this.region = region;
        this.town = town;
        this.travellingInfo = travellingInfo;
        this.openTime = openTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.website = website;
        this.parkingInfo = parkingInfo;
        this.ticketInfo = ticketInfo;
        this.remarks = remarks;
        this.picture1 = picture1;
    }


}
