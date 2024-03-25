package com.dbproject.api.invitePlan.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitedPlanListResponse {

//    초대받은 Plan 목록
    List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();



}
