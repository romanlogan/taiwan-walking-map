package com.dbproject.api.invitePlan.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RejectInvitePlanRequest {

    @NotNull(message = "invitePlanId value is required")
    Integer invitePlanId;

    public RejectInvitePlanRequest() {

    }

    @Builder
    private RejectInvitePlanRequest(Integer invitePlanId) {
        this.invitePlanId = invitePlanId;
    }

    public static RejectInvitePlanRequest create(Integer invitePlanId) {

        return RejectInvitePlanRequest.builder()
                .invitePlanId(invitePlanId)
                .build();
    }

}
