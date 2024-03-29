package com.dbproject.api.invitePlan.service;


import com.dbproject.api.invitePlan.dto.AcceptInvitedPlanRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitedPlanListResponse;
import com.dbproject.api.invitePlan.dto.RejectInvitePlanRequest;

public interface InvitePlanService {

    Long invitePlan(InvitePlanRequest request,String email);

    InvitedPlanListResponse getInvitedList(String email);

    Long accept(AcceptInvitedPlanRequest request,String email);

    Long reject(RejectInvitePlanRequest request, String email);
}
