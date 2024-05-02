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
import com.dbproject.api.plan.repository.PlanRepository;
import com.dbproject.api.planMember.PlanMember;
import com.dbproject.api.planMember.repository.PlanMemberRepository;
import com.dbproject.api.route.Route;
import com.dbproject.api.route.RouteDto;
import com.dbproject.api.route.RouteRepository;
import com.dbproject.api.routeLocation.RouteLocation;
import com.dbproject.api.routeLocation.repository.RouteLocationRepository;
import com.dbproject.constant.InvitePlanStatus;
import com.dbproject.exception.InvitePlanNotExistException;
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
    private final RouteLocationRepository routeLocationRepository;
    private final RouteRepository routeRepository;
    private final PlanMemberRepository planMemberRepository;
    private final PlanRepository planRepository;

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
//            route 를 저장해서 id 가 있는 route 를 넘기기
            Route savedRoute = routeRepository.save(route);


            savedRoute.setRouteLocationList(getRouteLocationList(routeRequest,savedRoute));
            routeList.add(savedRoute);
        }

        savedInvitePlan.setRouteList(routeList);
    }

    private List<RouteLocation> getRouteLocationList(InvitePlanRouteRequest routeRequest, Route savedRoute) {

        List<RouteLocation> routeLocationList = new ArrayList<>();
        for (InvitePlanLocationRequest locationRequest : routeRequest.getLocationRequestList()) {

            Location location = getLocationFromRequest(locationRequest);

            RouteLocation routeLocation = RouteLocation.createRouteLocation(savedRoute, location);
            RouteLocation savedRouteLocation = routeLocationRepository.save(routeLocation);


            routeLocationList.add(savedRouteLocation);
        }

        return routeLocationList;
    }

    private Location getLocationFromRequest(InvitePlanLocationRequest locationRequest) {
        Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
        FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
        Location location = favoriteLocation.getLocation();
        return location;
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

            routeDtoList.add(routeDto);
        }

        return routeDtoList;
    }

    private static List<LocationDto> getLocationDtoList(Route route) {
        List<LocationDto> locationDtoList = new ArrayList<>();
        List<RouteLocation> routeLocationList = route.getRouteLocationList();

        for (RouteLocation routeLocation : routeLocationList) {

            LocationDto locationDto = LocationDto.of(routeLocation.getLocation());
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
        invitePlanMember.setInvitePlanStatus(InvitePlanStatus.ACCEPTED);        //나중에 InvitePlan 정보를 볼때 status 를 보고 요청 상태의 진행 상황을 확인 가능

        InvitePlan invitePlan = invitePlanMember.getInvitePlan();

        //     Plan 이 생성되어 있지 않은 경우 (가장 먼저 요청을 수락한 경우 )
        if(invitePlan.getInvitePlanStatus().equals(InvitePlanStatus.WAITING)){

//          친구 1명이라도 먼저 accept 를 하면 invitePlan 을 Plan 으로 바꾸기
            invitePlan.setInvitePlanStatus(InvitePlanStatus.ACCEPTED);
            Plan plan = Plan.createPlan(invitePlan);
            PlanMember savedPlanMember = planMemberRepository.save(PlanMember.createPlanMember(invitePlanMember, plan));
            plan.addPlanMember(savedPlanMember);
            planRepository.save(plan);

        } else if (invitePlan.getInvitePlanStatus().equals(InvitePlanStatus.ACCEPTED)) {
//            이미 Plan 이 생성되어 있는 경우
//            invitePlan 으로 먼저 Plan 을 찾아야한다 (방법 - plan 에 invitePlan 외래키를 넣는것 ? )
            Plan plan = planRepository.findByInvitePlan(invitePlan);
//            그 Plan 에다가 PlanMemberr 를 추가
            PlanMember savedPlanMember = planMemberRepository.save(PlanMember.createPlanMember(invitePlanMember, plan));
            plan.addPlanMember(savedPlanMember);
        }
//        새로운 Plan id 를 return
        return invitePlanMember.getId();
    }


    private static List<PlanMember> getPlanMemberList(InvitePlan invitePlan, Plan plan) {
        List<InvitePlanMember> inviteFriendList = invitePlan.getInviteFriendList();
        List<PlanMember> planMemberList = new ArrayList<>();
        for (InvitePlanMember member : inviteFriendList) {

            PlanMember planMember = PlanMember.createPlanMember(member, plan);
            planMemberList.add(planMember);
        }
        return planMemberList;
    }

    @Override
    public Long reject(RejectInvitePlanRequest request, String email) {

        InvitePlanMember invitePlanMember = invitePlanMemberRepository.getByIdAndEmail(Long.valueOf(request.getPlanId()), email);
        invitePlanMember.setInvitePlanStatus(InvitePlanStatus.REJECTED);

        return invitePlanMember.getId();
    }

    @Override
    public SentInvitePlanListResponse getSentInviteList(String email) {

        SentInvitePlanListResponse response = new SentInvitePlanListResponse();

        Member requester = memberRepository.findByEmail(email);
        List<InvitePlan> invitePlanList = invitePlanRepository.findByRequester(requester);

        List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();
//        dto 로 변
        for (InvitePlan invitePlan : invitePlanList) {

            InvitePlanDto invitePlanDto = InvitePlanDto.of(invitePlan);
            invitePlanDto.setInvitePlanMemberDtoList(getInvitePlanMemberDtoList(invitePlan));
            invitePlanDto.setRouteDtoList(getRouteDtoList(invitePlan));

            invitePlanDtoList.add(invitePlanDto);
        }

        response.setInvitePlanDtoList(invitePlanDtoList);

        return response;
    }

    @Override
    public GetInvitePlanResponse getInvitePlanDtl(Integer id) {

        Optional<InvitePlan> optionalInvitePlan = invitePlanRepository.findById(Long.valueOf(id));
        if (optionalInvitePlan.isEmpty()) {
            throw new InvitePlanNotExistException("InvitePlan 이 존재하지 않습니다.");
        }

        InvitePlan invitePlan = optionalInvitePlan.get();
        InvitePlanDto invitePlanDto = InvitePlanDto.of(invitePlan);

        return GetInvitePlanResponse.createResponse(invitePlanDto);
    }

}
