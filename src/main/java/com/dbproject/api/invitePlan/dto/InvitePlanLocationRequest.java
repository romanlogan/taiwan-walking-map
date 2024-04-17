package com.dbproject.api.invitePlan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitePlanLocationRequest {


    private Integer favoriteLocationId;


    public InvitePlanLocationRequest() {
    }

    public InvitePlanLocationRequest(Integer favoriteLocationId) {
        this.favoriteLocationId = favoriteLocationId;
    }
}
