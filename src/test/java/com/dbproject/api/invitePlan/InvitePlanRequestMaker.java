package com.dbproject.api.invitePlan;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRouteRequest;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.constant.PlanPeriod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class InvitePlanRequestMaker {

// favoriteRepository 가 null
    @Autowired
    private static FavoriteRepository favoriteRepository;

    public static InvitePlanRequest createForTest(String name, PlanPeriod planPeriod, String supply, LocalDate departDate, Integer tripDay, List<String> friendEmailList) {

        InvitePlanRequest invitePlanRequest = InvitePlanRequest.of(
                name,
                planPeriod,
                supply,
                departDate,
                3);


        setMemberListForTest(invitePlanRequest,friendEmailList);
        setRouteListForTest(invitePlanRequest);

        return invitePlanRequest;
    }

    public static void setMemberListForTest(InvitePlanRequest invitePlanRequest, List<String> friendEmailList){

        List<InvitePlanMemberRequest> requestList = new ArrayList<>();

        for (String friendEmail : friendEmailList) {
            InvitePlanMemberRequest request = new InvitePlanMemberRequest(friendEmail);
            requestList.add(request);
        }
        invitePlanRequest.setMemberList(requestList);
    }

    public static void setRouteListForTest(InvitePlanRequest invitePlanRequest) {

        List<InvitePlanRouteRequest> routeRequestList = new ArrayList<>();

        InvitePlanRouteRequest routeRequest = new InvitePlanRouteRequest(1);
        routeRequest.setLocationRequestList(getInvitePlanLocationRequestList());

        routeRequestList.add(routeRequest);
        invitePlanRequest.setRouteList(routeRequestList);
    }

    private static List<InvitePlanLocationRequest> getInvitePlanLocationRequestList() {

        List<InvitePlanLocationRequest> invitePlanLocationRequestList = new ArrayList<>();

//    여기 리포지토리를 찾는 부분이 문제
        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findByMemberEmail("asdf@asdf.com");

        for (FavoriteLocation favoriteLocation : favoriteLocationList) {

            InvitePlanLocationRequest invitePlanLocationRequest = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocation.getId()));
            invitePlanLocationRequestList.add(invitePlanLocationRequest);
        }

        return invitePlanLocationRequestList;
    }
}
