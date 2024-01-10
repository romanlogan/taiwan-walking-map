package com.dbproject.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;


@Getter
@Setter
@NoArgsConstructor
public class RecLocationListResponse {

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

    @QueryProjection
    public RecLocationListResponse(String locationId, String picture1, String name, String address, String openTime, String ticketInfo, String website, String tel, String latitude, String longitude) {
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
    }
}
