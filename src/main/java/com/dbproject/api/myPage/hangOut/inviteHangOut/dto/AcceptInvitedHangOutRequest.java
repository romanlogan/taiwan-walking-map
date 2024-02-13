package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcceptInvitedHangOutRequest {

    private Integer inviteHangOutId;

    public AcceptInvitedHangOutRequest(Integer inviteHangOutId) {
        this.inviteHangOutId = inviteHangOutId;
    }
}
