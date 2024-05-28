package com.dbproject.api.invitePlan.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitePlanRouteRequest {

    private Integer day;

//    하루에 최대 10곳 선택 가능
    private List<InvitePlanLocationRequest> locationRequestList = new ArrayList<>();


    public InvitePlanRouteRequest() {
    }

    public InvitePlanRouteRequest(Integer day) {
        this.day = day;
    }

    @Builder
    public InvitePlanRouteRequest(Integer day, List<InvitePlanLocationRequest> locationRequestList) {
        this.day = day;
        this.locationRequestList = locationRequestList;
    }

    public static InvitePlanRouteRequest create(Integer day, List<InvitePlanLocationRequest> locationRequestList) {

        return InvitePlanRouteRequest.builder()
                .day(day)
                .locationRequestList(locationRequestList)
                .build();
    }
}
