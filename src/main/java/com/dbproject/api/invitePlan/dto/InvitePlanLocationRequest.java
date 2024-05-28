package com.dbproject.api.invitePlan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitePlanLocationRequest {


    private Integer favoriteLocationId;


    public InvitePlanLocationRequest() {
    }

    @Builder
    public InvitePlanLocationRequest(Integer favoriteLocationId) {
        this.favoriteLocationId = favoriteLocationId;
    }

    public static InvitePlanLocationRequest create(Integer favoriteLocationId) {

        return InvitePlanLocationRequest.builder()
                .favoriteLocationId(favoriteLocationId)
                .build();
    }
}
