package com.dbproject.web.invitePlan;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.service.FriendService;
import com.dbproject.api.invitePlan.dto.AcceptInvitedPlanRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitedPlanListResponse;
import com.dbproject.api.invitePlan.dto.RejectInvitePlanRequest;
import com.dbproject.api.invitePlan.service.InvitePlanService;
import com.dbproject.binding.CheckBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.kerberos.KerberosTicket;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class InvitePlanController {

    private final InvitePlanService invitePlanService;
    private final FriendService friendService;

    @GetMapping("/")
    public String getPlanForm(Principal principal,
                              Model model) {

        String email = principal.getName();
        FriendListResponse friendListResponse = friendService.getFriendList(email);

        model.addAttribute("friendListResponse", friendListResponse);

        return "plan/planForm";
    }

    @PostMapping("/invite")
    public ResponseEntity invite(@Valid @RequestBody InvitePlanRequest request,
                                 BindingResult bindingResult,
                                 Principal principal) {

        System.out.println("------------------------------------------------------------------");
        System.out.println(request.toString());

        String email = principal.getName();

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        Long invitePlanId = invitePlanService.invitePlan(request,email);

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(invitePlanId),
                null
        ), HttpStatus.OK);
    }

    @GetMapping("/invitedList")
    public String getInvitedList(Principal principal, Model model) {

        String email = principal.getName();
        InvitedPlanListResponse response = invitePlanService.getInvitedList(email);

        model.addAttribute("response", response);

        return "plan/invitedPlanList";
    }

    @PostMapping("/accept")
    public ResponseEntity<ApiResponse> acceptInvitedPlan(@RequestBody AcceptInvitedPlanRequest request,
                                                         Principal principal) {
        String email = principal.getName();
        Long id = invitePlanService.accept(request, email);

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(id),
                null
        ), HttpStatus.OK);
    }

    @PostMapping("/reject")
    public ResponseEntity<ApiResponse> rejectInvitedPlan(@RequestBody RejectInvitePlanRequest request,
                                                         Principal principal) {
        String email = principal.getName();
        Long id = invitePlanService.reject(request, email);

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(id),
                null
        ), HttpStatus.OK);
    }
}



















