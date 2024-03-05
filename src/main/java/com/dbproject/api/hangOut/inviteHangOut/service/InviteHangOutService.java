package com.dbproject.api.hangOut.inviteHangOut.service;

import com.dbproject.api.hangOut.inviteHangOut.dto.*;

import java.util.Optional;

public interface InviteHangOutService {

    Long inviteHangOut(InviteHangOutRequest inviteHangOutRequest, String email);
    InvitedHangOutResponse getInvitedHangOutList(String email, Optional<Long> optionalInviteHangOutId);
    void acceptInvitedHangOut(AcceptInvitedHangOutRequest acceptInvitedHangOutRequest);
    void rejectInvitedHangOut(RejectInvitedHangOutRequest rejectInvitedHangOutRequest);
    void inviteFromLocationPage(InviteHangOutFromLocRequest inviteHangOutFromLocRequest, String email);
}
