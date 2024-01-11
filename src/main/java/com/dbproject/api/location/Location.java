package com.dbproject.api.location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "location")
@Getter
@Setter
public class Location {

    @Id
    private String locationId;

    private String name;

    private String zone;

    private String tolDescribe;

    private String description;

    private String tel;

    private String address;

    private String zipcode;

    private String region;

    private String town;

    private String travellingInfo;

    private String openTime;

    @Embedded
    private LocationPicture locationPicture;

//    private String picture1;
//
//    private String picDescribe1;
//
//    private String picture2;
//
//    private String picDescribe2;
//
//    private String picture3;
//
//    private String picDescribe3;

    private String map;

    private String gov;

    private String longitude;

    private String latitude;

    @Embedded
    private LocationClass locationClass;

//    private String orgclass;
//
//    private String class1;
//
//    private String class2;
//
//    private String class3;

    private String level;

    private String website;

    private String parkingInfo;

    private String parkingInfoPx;

    private String parkingInfoPy;

    private String ticketInfo;

    private String remarks;

    private String keyword;

    private String changeTime;

    public LocationPicture getLocationPictureMethod() {
        return this.locationPicture;
    }
}




















