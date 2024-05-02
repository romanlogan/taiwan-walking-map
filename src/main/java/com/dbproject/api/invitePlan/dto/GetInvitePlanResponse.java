package com.dbproject.api.invitePlan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetInvitePlanResponse {

    private InvitePlanDto invitePlanDto;

    public GetInvitePlanResponse() {
    }

    @Builder
    public GetInvitePlanResponse(InvitePlanDto invitePlanDto) {
        this.invitePlanDto = invitePlanDto;
    }

    public static GetInvitePlanResponse createResponse(InvitePlanDto invitePlanDto) {

        return GetInvitePlanResponse.builder()
                .invitePlanDto(invitePlanDto)
                .build();
    }
}
