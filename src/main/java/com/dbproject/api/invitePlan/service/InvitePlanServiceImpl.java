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


//
    @Override
    public Long invitePlan(InvitePlanRequest request,String requesterEmail) {

        InvitePlan savedInvitePlan = getSavedInvitePlan(request, requesterEmail);

        //request 를 그대로 보내면 메서드가 각 reqeust 에 종속되므로 request 를 다른 dto 로 변환하여 보내기
        setMemberListTo(savedInvitePlan, request);
        setRouteListTo(savedInvitePlan, request);

        return savedInvitePlan.getId();
    }


    public void setMemberListTo(InvitePlan savedInvitePlan, InvitePlanRequest request){

        List<InvitePlanMember> members = new ArrayList<>();

        for (InvitePlanMemberRequest memberRequest: request.getMemberList()) {

            createAndSaveAndAddMemberFromRequest(savedInvitePlan, members, memberRequest);
        }
        savedInvitePlan.setMembers(members);
    }

    private void createAndSaveAndAddMemberFromRequest(InvitePlan savedInvitePlan, List<InvitePlanMember> members, InvitePlanMemberRequest memberRequest) {
        //0.Member 찾기
        Member member = memberRepository.findByEmail(memberRequest.getFriendEmail());
        //1. InvitePlanMember 생성 및 저장
        InvitePlanMember invitePlanMember = createAndSaveInvitePlanMember(savedInvitePlan, member);
        //2. InvitePlanMemberList 에 InvitePlanMember  추가
        members.add(invitePlanMember);
    }

    private InvitePlanMember createAndSaveInvitePlanMember(InvitePlan savedInvitePlan, Member member) {
        InvitePlanMember invitePlanMember = InvitePlanMember.createWithoutSupply(member, savedInvitePlan);
        invitePlanMemberRepository.save(invitePlanMember);
        return invitePlanMember;
    }

    public void setRouteListTo(InvitePlan savedInvitePlan, InvitePlanRequest request) {

        List<Route> routeList = new ArrayList<>();

//        2일 이상의 여행에서는 루트도 2개 이상
        for (InvitePlanRouteRequest routeRequest : request.getRouteList()) {

            //            route 를 저장해서 id 가 있는 route 를 넘기기
            Route savedRoute = routeRepository.save(Route.createRoute(routeRequest));


            savedRoute.setRouteLocationList(getRouteLocationList(routeRequest,savedRoute));
            routeList.add(savedRoute);
        }

        savedInvitePlan.setRoutes(routeList);
    }

    private List<RouteLocation> getRouteLocationList(InvitePlanRouteRequest routeRequest, Route route) {

        List<RouteLocation> routeLocationList = new ArrayList<>();

        for (InvitePlanLocationRequest locationRequest : routeRequest.getLocationRequestList()) {

            Location location = getLocationFromRequest(locationRequest);

            RouteLocation routeLocation = RouteLocation.createRouteLocation(route, location);
            routeLocationList.add(routeLocationRepository.save(routeLocation));
        }

        return routeLocationList;
    }

    private Location getLocationFromRequest(InvitePlanLocationRequest locationRequest) {
        Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
        return optionalFavoriteLocation.get().getLocation();
    }

    private InvitePlan getSavedInvitePlan(InvitePlanRequest request, String email) {

        //id 가 존재하는 InvitePlan 을 위해 InvitePlan 생성 및 저장
        return invitePlanRepository.save(InvitePlan.createFrom(request, memberRepository.findByEmail(email)));
    }


    @Override
    public InvitedPlanListResponse getInvitedList(String email) {

        // fetch 조인 필요
        List<InvitePlanDto> invitePlanDtos = getInvitePlanDtosBy(email);

        return InvitedPlanListResponse.create(invitePlanDtos);
    }

    private List<InvitePlanDto> getInvitePlanDtosBy(String email) {

        List<InvitePlanDto> invitePlanDtos = new ArrayList<>();
        addInvitePlanDtoTo(invitePlanDtos, email);

        return invitePlanDtos;
    }

    private void addInvitePlanDtoTo(List<InvitePlanDto> invitePlanDtos, String email) {

        List<InvitePlanMember> members = invitePlanMemberRepository.getInvitedPlanMemberListByEmail(email);
        // 1. 유저의 요청받은 plan 목록

        for (InvitePlanMember member : members) {

            InvitePlan invitePlan = member.getInvitePlan();
            InvitePlanDto invitePlanDto = createInvitePlanDtoFrom(invitePlan);
            invitePlanDtos.add(invitePlanDto);
        }
    }

    private InvitePlanDto createInvitePlanDtoFrom(InvitePlan invitePlan) {
        InvitePlanDto invitePlanDto = InvitePlanDto.from(invitePlan);
        invitePlanDto.setInvitePlanMemberDtoList(getInvitePlanMemberDtoList(invitePlan));
        invitePlanDto.setRouteDtoList(getRouteDtoList(invitePlan));
        return invitePlanDto;
    }


    @Override
    public Long accept(AcceptInvitedPlanRequest request, String email) {

//        1. Accept 상태로 바꾸기 (언제 삭제를 할까 ? 바로 or 일정 기간 후)
        InvitePlanMember invitePlanMember = invitePlanMemberRepository.getByIdAndEmail(Long.valueOf(request.getInvitedPlanId()), email);
        invitePlanMember.setStatus(InvitePlanStatus.ACCEPTED);        //나중에 InvitePlan 정보를 볼때 status 를 보고 요청 상태의 진행 상황을 확인 가능

        InvitePlan invitePlan = invitePlanMember.getInvitePlan();

        if(invitePlan.isStatusWaiting()){
            //     Plan 이 생성되어 있지 않은 경우 (가장 먼저 요청을 수락한 경우 )
//          친구 1명이라도 먼저 accept 를 하면
//          1. invitePlan stats 를 accept 로 변경
            setStatusToAcceptWhenFriendAcceptPlan(invitePlan);

//          2. invitePlan 으로 Plan 을 생성
            Plan plan = createAndSavePlanFrom(invitePlan);
            createPlanMemberAndAddTo(plan,invitePlanMember);

        } else if (invitePlan.isStatusAccepted()) {
//            이미 Plan 이 생성되어 있는 경우
            Plan plan = planRepository.findByInvitePlan(invitePlan);
            createPlanMemberAndAddTo(plan,invitePlanMember);
        }

        return invitePlanMember.getId();
    }

    private void createPlanMemberAndAddTo(Plan plan, InvitePlanMember invitePlanMember) {
        PlanMember planMember = planMemberRepository.save(PlanMember.createPlanMember(invitePlanMember, plan));
        plan.addPlanMember(planMember);
    }

    private static void setStatusToAcceptWhenFriendAcceptPlan(InvitePlan invitePlan) {
        invitePlan.setInvitePlanStatus(InvitePlanStatus.ACCEPTED);
    }

    private Plan createAndSavePlanFrom(InvitePlan invitePlan) {
        Plan plan = Plan.createPlan(invitePlan);
        planRepository.save(plan);
        return plan;
    }

    @Override
    public Long reject(RejectInvitePlanRequest request, String email) {

//        1. watting - 아무도 아직 수락, 거절을 안함
//        2. accepted - 최소 한명은 이미 수락함
//        3. rejected - 만약 모든 member 가 거절하면 rejected ?

        InvitePlanMember invitePlanMember = invitePlanMemberRepository.getByIdAndEmail(Long.valueOf(request.getInvitePlanId()), email);
        invitePlanMember.setStatus(InvitePlanStatus.REJECTED);

        InvitePlan invitePlan = invitePlanMember.getInvitePlan();
        List<InvitePlanMember> members = invitePlan.getMembers();

        changeStatusToRejected(invitePlan, members);

        return invitePlanMember.getId();
    }

    private static void changeStatusToRejected(InvitePlan invitePlan, List<InvitePlanMember> members) {
        int count = 0;
        for (InvitePlanMember member : members) {
            if (memberStatusIsWaitingOrAccepted(member)) {
                count++;
            }
        }

        if (count == 0) {
            invitePlan.setInvitePlanStatus(InvitePlanStatus.REJECTED);
        }
    }

    private static boolean memberStatusIsWaitingOrAccepted(InvitePlanMember member) {
        return member.isStatusWaiting() || member.isStatusAccepted();
    }

    @Override
    public SentInvitePlanListResponse getSentInviteList(String email) {

        SentInvitePlanListResponse response = new SentInvitePlanListResponse();

        Member requester = memberRepository.findByEmail(email);
        List<InvitePlan> invitePlanList = invitePlanRepository.findByRequester(requester);

        List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();
//        dto 로 변
        for (InvitePlan invitePlan : invitePlanList) {

            InvitePlanDto invitePlanDto = createInvitePlanDtoFrom(invitePlan);
            invitePlanDtoList.add(invitePlanDto);
        }

        response.setInvitePlanDtoList(invitePlanDtoList);

        return response;
    }

    @Override
    public InvitePlanDtlResponse getInvitePlanDtl(Integer id) {

        Optional<InvitePlan> optionalInvitePlan = invitePlanRepository.findById(Long.valueOf(id));

        checkIsExistFor(optionalInvitePlan);

        InvitePlanDto invitePlanDto = InvitePlanDto.from(optionalInvitePlan.get());

        List<InvitePlanMember> invitePlanMemberList = optionalInvitePlan.get().getMembers();
        invitePlanDto.setInvitePlanMemberDtoListBy(invitePlanMemberList);

        List<RouteDto> routeDtoList = RouteDto.createRouteDtosFrom(optionalInvitePlan.get().getRoutes());
        invitePlanDto.setRouteDtoList(routeDtoList);

        return InvitePlanDtlResponse.createResponse(invitePlanDto);
    }

    private static void checkIsExistFor(Optional<InvitePlan> optionalInvitePlan) {
        if (optionalInvitePlan.isEmpty()) {
            throw new InvitePlanNotExistException("InvitePlan 이 존재하지 않습니다.");
        }
    }


    private List<RouteDto> getRouteDtoList(InvitePlan invitePlan) {

        List<RouteDto> routeDtoList = new ArrayList<>();
        for (Route route : invitePlan.getRoutes()) {

            RouteDto routeDto = RouteDto.createRouteDto(route);
            routeDto.setLocationDtos(getLocationDtoList(route));

            routeDtoList.add(routeDto);
        }

        return routeDtoList;
    }

    private static List<LocationDto> getLocationDtoList(Route route) {
        List<LocationDto> locationDtoList = new ArrayList<>();
        List<RouteLocation> routeLocationList = route.getRouteLocationList();

        for (RouteLocation routeLocation : routeLocationList) {

            LocationDto locationDto = LocationDto.from(routeLocation.getLocation());
            locationDtoList.add(locationDto);
        }
        return locationDtoList;
    }

    private static List<InvitePlanMemberDto> getInvitePlanMemberDtoList(InvitePlan invitePlan) {
        List<InvitePlanMemberDto> invitePlanMemberDtoList = new ArrayList<>();
        List<InvitePlanMember> invitePlanMemberList = invitePlan.getMembers();
        for (InvitePlanMember planMember : invitePlanMemberList) {

            InvitePlanMemberDto invitePlanMemberDto = InvitePlanMemberDto.of(planMember.getMember());
            invitePlanMemberDtoList.add(invitePlanMemberDto);
        }
        return invitePlanMemberDtoList;
    }

    private static List<PlanMember> getPlanMemberList(InvitePlan invitePlan, Plan plan) {
        List<InvitePlanMember> inviteFriendList = invitePlan.getMembers();
        List<PlanMember> planMemberList = new ArrayList<>();
        for (InvitePlanMember member : inviteFriendList) {

            PlanMember planMember = PlanMember.createPlanMember(member, plan);
            planMemberList.add(planMember);
        }
        return planMemberList;
    }


    private Route getSavedRoute(InvitePlanRouteRequest routeRequest) {
        Route route = Route.createRoute(routeRequest);
        return routeRepository.save(route);
    }
}
