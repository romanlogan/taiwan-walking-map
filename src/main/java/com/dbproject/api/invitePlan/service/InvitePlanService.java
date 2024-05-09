package com.dbproject.api.invitePlan.service;


import com.dbproject.api.invitePlan.dto.*;

public interface InvitePlanService {

    Long invitePlan(InvitePlanRequest request,String email);

    InvitedPlanListResponse getInvitedList(String email);

    Long accept(AcceptInvitedPlanRequest request,String email);

    Long reject(RejectInvitePlanRequest request, String email);

    SentInvitePlanListResponse getSentInviteList(String email);

    InvitePlanDtlResponse getInvitePlanDtl(Integer id);



}
