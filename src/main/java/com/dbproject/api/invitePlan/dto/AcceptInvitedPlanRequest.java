package com.dbproject.api.invitePlan.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AcceptInvitedPlanRequest {

    @NotNull(message = "invitedPlanId value is required")
    Integer invitedPlanId;

    public AcceptInvitedPlanRequest() {
    }

    @Builder
    public AcceptInvitedPlanRequest(Integer invitedPlanId) {
        this.invitedPlanId = invitedPlanId;
    }

    public static AcceptInvitedPlanRequest create(Integer invitedPlanId) {
        return AcceptInvitedPlanRequest.builder()
                .invitedPlanId(invitedPlanId)
                .build();
    }
}
