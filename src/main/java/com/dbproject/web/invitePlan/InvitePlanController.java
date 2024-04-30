package com.dbproject.web.invitePlan;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.service.FriendService;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.AcceptInvitedPlanRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitedPlanListResponse;
import com.dbproject.api.invitePlan.dto.RejectInvitePlanRequest;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.security.auth.kerberos.KerberosTicket;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
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

    @GetMapping("/test")
    public void test(Principal principal) {

        Optional<InvitePlan> optionalInvitePlan = invitePlanRepository.findById(9999L);
        InvitePlan invitePlan = optionalInvitePlan.get();

        List<Route> routeList = invitePlan.getRouteList();

        for (Route route : routeList) {

            System.out.println("route ID = "+route.getId());

            List<RouteLocation> routeLocationList = route.getRouteLocationList();

            for (RouteLocation routeLocation : routeLocationList) {

                Location location = routeLocation.getLocation();
                System.out.println(location.getName());

            }
        }



    }
}



















