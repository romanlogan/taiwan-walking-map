package com.dbproject.api.favorite.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FavoriteLocationList {

    private Long favoriteLocationId;
    private String locationId;
    private String name;
    private String picture1;
    private String longitude;
    private String latitude;

    private String memo;

    private String ticketInfo;
    private String region;
    private String openTime;


    public FavoriteLocationList(String locationId, String name) {
        this.locationId = locationId;
        this.name = name;
    }

    @QueryProjection
    public FavoriteLocationList(String locationId, String name, String region, String openTime, String picture1, String longitude, String latitude, String ticketInfo, String memo, Long favoriteLocationId) {
        this.locationId = locationId;
        this.name = name;
        this.region = region;
        this.openTime = openTime;
        this.picture1 = picture1;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ticketInfo = ticketInfo;
        this.memo = memo;
        this.favoriteLocationId = favoriteLocationId;
    }
}
