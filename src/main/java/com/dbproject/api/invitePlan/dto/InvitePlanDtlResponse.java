package com.dbproject.api.invitePlan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitePlanDtlResponse {

    private InvitePlanDto invitePlanDto;

    public InvitePlanDtlResponse() {
    }

    @Builder
    public InvitePlanDtlResponse(InvitePlanDto invitePlanDto) {
        this.invitePlanDto = invitePlanDto;
    }

    public static InvitePlanDtlResponse createResponse(InvitePlanDto invitePlanDto) {

        return InvitePlanDtlResponse.builder()
                .invitePlanDto(invitePlanDto)
                .build();
    }
}
