package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RejectInvitedHangOutRequest {

    private Long inviteHangOutId;

    public RejectInvitedHangOutRequest(Long inviteHangOutId) {
        this.inviteHangOutId = inviteHangOutId;
    }
}
