package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcceptInvitedHangOutRequest {

    private Integer inviteHangOutId;

    public AcceptInvitedHangOutRequest() {
    }

    public AcceptInvitedHangOutRequest(Integer inviteHangOutId) {
        this.inviteHangOutId = inviteHangOutId;
    }
}
