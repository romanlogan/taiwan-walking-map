package com.dbproject.web.plan;

import com.dbproject.api.invitePlan.service.InvitePlanServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PlanController {

    private final InvitePlanServiceImpl planService;

//    @PostMapping
//    public ResponseEntity invitePlan(@Valid @RequestBody InvitePlanRequest invitePlanRequest) {
//
//
//    }
}
