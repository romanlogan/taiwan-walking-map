package com.dbproject.api.invitePlan.dto;


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
}
