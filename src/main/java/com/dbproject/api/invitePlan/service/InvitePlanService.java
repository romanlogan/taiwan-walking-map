package com.dbproject.api.invitePlan.service;


import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitedPlanListResponse;

public interface InvitePlanService {

    Long invitePlan(InvitePlanRequest request,String email);

    InvitedPlanListResponse getInvitedList(String email);
}
