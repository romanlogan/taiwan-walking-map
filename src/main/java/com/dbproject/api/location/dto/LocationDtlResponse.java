package com.dbproject.api.location.dto;

import com.dbproject.api.comment.dto.CommentDto;
import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.location.Location;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class LocationDtlResponse {

    private boolean isSaved;
    private String id;
    private String name;
    private String tolDescribe;
    private String address;
    private String region;
    private String openTime;
    private String ticketInfo;
    private String website;
    private String tel;
    private String remarks;
    private String travellingInfo;
    private String parkingInfo;
    private String picture1;
    private String longitude;
    private String latitude;

    private List<CommentDto> commentDtoList = new ArrayList<>();

    private List<FriendDto> friendDtoList = new ArrayList<>();

    @Builder
    public LocationDtlResponse(String id, String name, String tolDescribe, String address, String region,String openTime, String ticketInfo, String website, String tel, String remarks, String travellingInfo, String parkingInfo, String picture1, String longitude, String latitude) {
        this.id = id;
        this.name = name;
        this.tolDescribe = tolDescribe;
        this.address = address;
        this.region = region;
        this.openTime = openTime;
        this.ticketInfo = ticketInfo;
        this.website = website;
        this.tel = tel;
        this.remarks = remarks;
        this.travellingInfo = travellingInfo;
        this.parkingInfo = parkingInfo;
        this.picture1 = picture1;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static LocationDtlResponse create(Location location) {

        return LocationDtlResponse.builder()
                .id(location.getLocationId())
                .name(location.getName())
                .tolDescribe(location.getTolDescribe())
                .address(location.getAddress())
                .region(location.getRegion())
                .openTime(location.getOpenTime())
                .ticketInfo(location.getTicketInfo())
                .website(location.getWebsite())
                .tel(location.getTel())
                .remarks(location.getRemarks())
                .travellingInfo(location.getTravellingInfo())
                .parkingInfo(location.getParkingInfo())
//                .picture1(location.getPicture1())
                .picture1(location.getLocationPicture().getPicture1())
                .longitude(location.getLongitude())
                .latitude(location.getLatitude())
                .build();
    }

    public void setSaved(){
        this.isSaved = true;
    }

    public void setNotSaved(){
        this.isSaved = false;
    }
}
