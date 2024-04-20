package com.dbproject.api.invitePlan.service;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.*;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.api.invitePlan.invitePlanMember.repository.InvitePlanMemberRepository;
import com.dbproject.api.invitePlan.repository.InvitePlanRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.plan.Plan;
import com.dbproject.api.route.Route;
import com.dbproject.api.route.RouteDto;
import com.dbproject.constant.InvitePlanStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class InvitePlanServiceImpl implements InvitePlanService {

    private final InvitePlanRepository invitePlanRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final InvitePlanMemberRepository invitePlanMemberRepository;
    private final FavoriteRepository favoriteRepository;

    public Long invitePlan(InvitePlanRequest request,String email) {

        Member requester = memberRepository.findByEmail(email);
        //0. InvitePlan 생성
        InvitePlan invitePlan = InvitePlan.createInvitePlan(request,requester);
        //1.InvitePlan 저장
        InvitePlan savedInvitePlan = invitePlanRepository.save(invitePlan);

        //2. locationList 생성 및 InvitePlan 에 저장
        setRouteList(savedInvitePlan, request);

        //3. InvitePlanMemberList 생성 및 InvitePlan 에 저장
        setInvitePlanMemberList(savedInvitePlan,request);

        return savedInvitePlan.getId();
    }


    public void setRouteList(InvitePlan savedInvitePlan, InvitePlanRequest request) {

        List<Route> routeList = new ArrayList<>();

//        2일 이상의 여행에서는 루트도 2개 이상
        for (InvitePlanRouteRequest routeRequest : request.getInvitePlanRouteRequestList()) {

            Route route = Route.createRoute(routeRequest);
            List<Location> locationList = new ArrayList<>();

            for (InvitePlanLocationRequest locationRequest : routeRequest.getLocationRequestList()) {

                Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
                FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
                Location location = favoriteLocation.getLocation();
                locationList.add(location);
            }

            route.setLocationList(locationList);
            routeList.add(route);
        }

//        List<Location> locationList = new ArrayList<>();
//        for (InvitePlanLocationRequest locationRequest : request.getInvitePlanLocationRequestList()) {
//            //            fetch 조인으로 바꿀 필요
//            Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
//            FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
//            Location location = favoriteLocation.getLocation();
//
////            Location location = locationRepository.findByLocationId();       //savedInvitePlan 이 id 가 0인데 이 아이디가 0인 savedInvitePlan 을 찾아르 수 없다
//            locationList.add(location);
//        }
//        savedInvitePlan.setLocationList(locationList);
    }



    public void setInvitePlanMemberList(InvitePlan savedInvitePlan, InvitePlanRequest request){
        List<InvitePlanMember> invitePlanMemberList = new ArrayList<>();
        for (InvitePlanMemberRequest memberRequest: request.getInvitePlanMemberRequestList()) {
            //0.Member 찾기
            String email = memberRequest.getFriendEmail();
            Member member = memberRepository.findByEmail(email);

            //1. InvitePlanMember 생성 및 저장
            InvitePlanMember invitePlanMember = InvitePlanMember.createInvitePlanMemberWithoutSupply(member, savedInvitePlan);
            invitePlanMemberRepository.save(invitePlanMember);

            //2. InvitePlanMemberList 에 InvitePlanMember  추가
            invitePlanMemberList.add(invitePlanMember);
        }
        savedInvitePlan.setInviteFriendList(invitePlanMemberList);
    }

    @Override
    public InvitedPlanListResponse getInvitedList(String email) {

        // fetch 조인 필요
        InvitedPlanListResponse response = new InvitedPlanListResponse();
        List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();


        List<InvitePlanMember> invitedPlanMemberList = invitePlanMemberRepository.getInvitedPlanMemberListByEmail(email);
        // 1. 유저의 요청받은 plan 목록
        for (InvitePlanMember invitePlanMember : invitedPlanMemberList) {

            InvitePlan invitePlan = invitePlanMember.getInvitePlan();

            InvitePlanDto invitePlanDto = InvitePlanDto.of(invitePlan);
            invitePlanDto.setInvitePlanMemberDtoList(getInvitePlanMemberDtoList(invitePlan));
            invitePlanDto.setRouteDtoList(getRouteDtoList(invitePlan));

            invitePlanDtoList.add(invitePlanDto);
        }

        response.setInvitePlanDtoList(invitePlanDtoList);

        return response;
    }

    private List<RouteDto> getRouteDtoList(InvitePlan invitePlan) {

        List<RouteDto> routeDtoList = new ArrayList<>();
        for (Route route : invitePlan.getRouteList()) {

            RouteDto routeDto = RouteDto.createRouteDto(route);
            routeDto.setLocationList(getLocationDtoList(route));
        }

    }

    private static List<LocationDto> getLocationDtoList(Route route) {
        List<LocationDto> locationDtoList = new ArrayList<>();
        List<Location> locationList = route.getLocationList();

        for (Location location : locationList) {

            LocationDto locationDto = LocationDto.of(location);
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }

    private static List<InvitePlanMemberDto> getInvitePlanMemberDtoList(InvitePlan invitePlan) {
        List<InvitePlanMemberDto> invitePlanMemberDtoList = new ArrayList<>();
        List<InvitePlanMember> invitePlanMemberList = invitePlan.getInviteFriendList();
        for (InvitePlanMember planMember : invitePlanMemberList) {

            InvitePlanMemberDto invitePlanMemberDto = InvitePlanMemberDto.of(planMember.getMember());
            invitePlanMemberDtoList.add(invitePlanMemberDto);
        }
        return invitePlanMemberDtoList;
    }

    @Override
    public Long accept(AcceptInvitedPlanRequest request, String email) {

//        1. Accept 상태로 바꾸기 (언제 삭제를 할까 ? 바로 or 일정 기간 후)
        InvitePlanMember invitePlanMember = invitePlanMemberRepository.getByIdAndEmail(Long.valueOf(request.getInvitedPlanId()), email);
        invitePlanMember.setInvitePlanStatus(InvitePlanStatus.ACCEPTED);

//        2. Plan 에 추가
        InvitePlan invitePlan = invitePlanMember.getInvitePlan();
        Plan plan = Plan.createPlan(invitePlan);



//        새로운 Plan id 를 return
        return invitePlanMember.getId();
    }

    @Override
    public Long reject(RejectInvitePlanRequest request, String email) {

        InvitePlanMember invitePlanMember = invitePlanMemberRepository.getByIdAndEmail(Long.valueOf(request.getPlanId()), email);
        invitePlanMember.setInvitePlanStatus(InvitePlanStatus.REJECTED);

        return invitePlanMember.getId();
    }
}
