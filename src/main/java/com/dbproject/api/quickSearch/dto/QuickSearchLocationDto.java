package com.dbproject.api.quickSearch.dto;

import com.dbproject.api.location.Location;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class QuickSearchLocationDto {


    private String locationId;
    private String picture1;
    private String name;
    private String address;
    private String openTime;
    private String ticketInfo;
    private String website;
    private String tel;
    private String latitude;
    private String longitude;
    private String region;


    @QueryProjection
    public QuickSearchLocationDto(String locationId, String picture1, String name, String address, String openTime, String ticketInfo, String website, String tel, String latitude, String longitude,String region) {
        this.locationId = locationId;
        this.picture1 = picture1;
        this.name = name;
        this.address = address;
        this.openTime = openTime;
        this.ticketInfo = ticketInfo;
        this.website = website;
        this.tel = tel;
        this.latitude = latitude;
        this.longitude = longitude;
        this.region = region;
    }

    public static QuickSearchLocationDto from(Location location) {

        QuickSearchLocationDto quickSearchLocationDto = new QuickSearchLocationDto();
        quickSearchLocationDto.locationId = location.getLocationId();
        quickSearchLocationDto.picture1 = location.getLocationPicture().getPicture1();
        quickSearchLocationDto.name = location.getName();
        quickSearchLocationDto.address = location.getAddress();
        quickSearchLocationDto.openTime = location.getOpenTime();
        quickSearchLocationDto.ticketInfo = location.getTicketInfo();
        quickSearchLocationDto.website = location.getWebsite();
        quickSearchLocationDto.tel = location.getTel();
        quickSearchLocationDto.latitude = location.getLatitude();
        quickSearchLocationDto.longitude = location.getLongitude();
        quickSearchLocationDto.region = location.getRegion();

        return quickSearchLocationDto;
    }

}
