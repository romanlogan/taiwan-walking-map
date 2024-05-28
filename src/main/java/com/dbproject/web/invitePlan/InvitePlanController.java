package com.dbproject.web.invitePlan;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.service.FriendService;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.*;
import com.dbproject.api.invitePlan.repository.InvitePlanRepository;
import com.dbproject.api.invitePlan.service.InvitePlanService;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.route.Route;
import com.dbproject.api.route.RouteRepository;
import com.dbproject.api.routeLocation.RouteLocation;
import com.dbproject.api.routeLocation.repository.RouteLocationRepository;
import com.dbproject.binding.CheckBindingResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/plan")
public class InvitePlanController {

    private final InvitePlanService invitePlanService;
    private final FriendService friendService;

    private final RouteLocationRepository routeLocationRepository;

    private final InvitePlanRepository invitePlanRepository;

    private final LocationRepository locationRepository;

    private final RouteRepository routeRepository;



    @GetMapping("/")
    public String getPlanForm(Principal principal,
                              Model model) {

        FriendListResponse response = friendService.getFriendList(principal.getName());

        model.addAttribute("friendListResponse", response);

        return "plan/planForm";
    }

    @PostMapping("/invite")
    public ResponseEntity invite(@Valid @RequestBody InvitePlanRequest request,
                                 BindingResult bindingResult,
                                 Principal principal) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceSuccessInAjax(bindingResult);
        }

        Long invitePlanId = invitePlanService.invitePlan(request, principal.getName());

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(invitePlanId),
                null
        ), HttpStatus.OK);
    }

    @GetMapping("/invitedList")
    public String getInvitedList(Principal principal, Model model) {

        InvitedPlanListResponse response = invitePlanService.getInvitedList(principal.getName());

        model.addAttribute("response", response);

        return "plan/invitedPlanList";
    }

    @PostMapping("/accept")
    public ResponseEntity<ApiResponse> acceptInvitedPlan(@Valid @RequestBody AcceptInvitedPlanRequest request,
                                                         BindingResult bindingResult,
                                                         Principal principal) {

        if(bindingResult.hasErrors()){
            return CheckBindingResult.induceErrorInAjax(bindingResult);
        }

        Long id = invitePlanService.accept(request, principal.getName());

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(id),
                null
        ), HttpStatus.OK);
    }

    @PostMapping("/reject")
    public ResponseEntity<ApiResponse> rejectInvitedPlan(@Valid @RequestBody RejectInvitePlanRequest request,
                                                         Principal principal) {
        String email = principal.getName();
        Long id = invitePlanService.reject(request, email);

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(id),
                null
        ), HttpStatus.OK);
    }

    @GetMapping("/test")
    public void test(Principal principal) {

        Optional<InvitePlan> optionalInvitePlan = invitePlanRepository.findById(9999L);
        InvitePlan invitePlan = optionalInvitePlan.get();

        List<Route> routeList = invitePlan.getRoutes();

        for (Route route : routeList) {

            System.out.println("route ID = "+route.getId());

            List<RouteLocation> routeLocationList = route.getRouteLocationList();

            for (RouteLocation routeLocation : routeLocationList) {

                Location location = routeLocation.getLocation();
                System.out.println(location.getName());

            }
        }
    }

    @GetMapping("/sentInviteList")
    public String getSentInviteList(Principal principal, Model model) {

        SentInvitePlanListResponse response = invitePlanService.getSentInviteList(principal.getName());

        model.addAttribute("response", response);

        return "plan/sentInviteList";
    }

    @GetMapping("/invitePlan/{id}")
    public String getInvitePlanDtl(@PathVariable Integer id, Model model) {

        InvitePlanDtlResponse response = invitePlanService.getInvitePlanDtl(id);

        model.addAttribute("response", response);

        return "plan/invitePlanDtl";
    }
}



















