package com.dbproject.api.invitePlan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitedPlanListResponse {

//    초대받은 Plan 목록
    List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();

    public InvitedPlanListResponse() {
    }

    @Builder
    public InvitedPlanListResponse(List<InvitePlanDto> invitePlanDtoList) {
        this.invitePlanDtoList = invitePlanDtoList;
    }

    public static InvitedPlanListResponse create(List<InvitePlanDto> invitePlanDtoList) {

        return InvitedPlanListResponse.builder()
                .invitePlanDtoList(invitePlanDtoList)
                .build();
    }
}
