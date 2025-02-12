package com.dbproject.api.invitePlan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SentInvitePlanListResponse {

    List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();

    public SentInvitePlanListResponse() {
    }

    public SentInvitePlanListResponse(List<InvitePlanDto> invitePlanDtoList) {
        this.invitePlanDtoList = invitePlanDtoList;
    }
}

