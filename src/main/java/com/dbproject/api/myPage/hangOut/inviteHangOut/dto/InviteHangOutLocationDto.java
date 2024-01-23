package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOut;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InviteHangOutLocationDto {

    private boolean isEmpty;
    private String locationId;
    private String name;
    private String tel;
    private String address;
    private String region;
    private String openTime;
    private String longitude;
    private String latitude;
    private String ticketInfo;
    private String picture1;


    @Builder
    public InviteHangOutLocationDto(boolean isEmpty, String locationId, String name, String tel, String address, String region, String openTime, String longitude, String latitude, String ticketInfo, String picture1) {
        this.isEmpty = isEmpty;
        this.locationId = locationId;
        this.name = name;
        this.tel = tel;
        this.address = address;
        this.region = region;
        this.openTime = openTime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ticketInfo = ticketInfo;
        this.picture1 = picture1;
    }


    public static InviteHangOutLocationDto builderTest(InviteHangOut inviteHangOut) {

        return InviteHangOutLocationDto.builder().build();
    }

    public static InviteHangOutLocationDto createEmptyDto() {

        return InviteHangOutLocationDto.builder()
                .isEmpty(true)
                .build();
    }

    public static InviteHangOutLocationDto from(InviteHangOut inviteHangOut) {

        return InviteHangOutLocationDto.builder()
                .isEmpty(false)
                .locationId(inviteHangOut.getFavoriteLocation().getLocation().getLocationId())
                .name(inviteHangOut.getFavoriteLocation().getLocation().getName())
                .tel(inviteHangOut.getFavoriteLocation().getLocation().getTel())
                .address(inviteHangOut.getFavoriteLocation().getLocation().getAddress())
                .region(inviteHangOut.getFavoriteLocation().getLocation().getRegion())
                .openTime(inviteHangOut.getFavoriteLocation().getLocation().getOpenTime())
                .longitude(inviteHangOut.getFavoriteLocation().getLocation().getLongitude())
                .latitude(inviteHangOut.getFavoriteLocation().getLocation().getLatitude())
                .ticketInfo(inviteHangOut.getFavoriteLocation().getLocation().getTicketInfo())
                .picture1(inviteHangOut.getFavoriteLocation().getLocation().getLocationPicture().getPicture1())
                .build();
    }

}
