package com.dbproject.api.invitePlan.invitePlanMember.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvitePlanMemberRequest {

    private String friendEmail;
//    private String friendSupply;

    public InvitePlanMemberRequest() {
    }

    public InvitePlanMemberRequest(String friendEmail, String friendSupply) {
        this.friendEmail = friendEmail;
//        this.friendSupply = friendSupply;
    }
}
