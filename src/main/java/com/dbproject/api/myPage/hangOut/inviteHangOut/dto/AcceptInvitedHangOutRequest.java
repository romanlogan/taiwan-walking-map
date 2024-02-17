package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AcceptInvitedHangOutRequest {

    @NotNull
    private Long inviteHangOutId;

    public AcceptInvitedHangOutRequest(Long inviteHangOutId) {
        this.inviteHangOutId = inviteHangOutId;
    }
}
