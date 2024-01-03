package com.dbproject.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteListResponse {

    //location 에서 필요한 attr 가져오기
    private String locationId;
    private String name;
    private String region;
    private String openTime;
    private String picture1;
    private String longitude;
    private String latitude;
    private String ticketInfo;


    @QueryProjection
    public FavoriteListResponse(String locationId, String name, String region, String openTime, String picture1, String longitude, String latitude,String ticketInfo) {
        this.locationId = locationId;
        this.name = name;
        this.region = region;
        this.openTime = openTime;
        this.picture1 = picture1;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ticketInfo = ticketInfo;
    }
}
