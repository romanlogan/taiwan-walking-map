package com.dbproject.api.invitePlan.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InvitePlanRouteRequest {

    private Integer day;

    private List<InvitePlanLocationRequest> locationRequestList;


}
