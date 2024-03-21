package com.dbproject.api.invitePlan.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitePlanLocationRequest {

    private String locationId;

    public InvitePlanLocationRequest() {
    }

    public InvitePlanLocationRequest(String locationId) {
        this.locationId = locationId;
    }
}
