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
        setLocationList(savedInvitePlan, request);

        //3. InvitePlanMemberList 생성 및 InvitePlan 에 저장
        setInvitePlanMemberList(savedInvitePlan,request);

        return savedInvitePlan.getId();
    }


    public void setLocationList(InvitePlan savedInvitePlan, InvitePlanRequest request) {

        List<Route> routeList = new ArrayList<>();

        for (InvitePlanRouteRequest routeRequest : request.getInvitePlanRouteRequestList()) {



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
        List<InvitePlanMember> invitedPlanMemberList = invitePlanMemberRepository.getInvitedPlanMemberListByEmail(email);
        InvitedPlanListResponse response = new InvitedPlanListResponse();
        List<InvitePlanDto> invitePlanDtoList = new ArrayList<>();

        // 1. 요청받은 plan 목록
        for (InvitePlanMember invitePlanMember : invitedPlanMemberList) {

            //2. plan 의 정보
            InvitePlan invitePlan = invitePlanMember.getInvitePlan();
            //요청자 정보
//            Member requester = memberRepository.findByEmail(invitePlan.getRequester().getEmail());

            InvitePlanDto invitePlanDto = new InvitePlanDto(
                    invitePlan.getId(),
                    invitePlan.getName(),
                    invitePlan.getPeriod(),
                    invitePlan.getSupply(),
                    invitePlan.getDepartDate(),
                    invitePlan.getArriveDate(),
                    invitePlan.getRequester().getEmail(),
                    invitePlan.getRequester().getName()
            );

            //3. plan 의 멤버 리스트
//            Member member = invitePlanMember.getMember();
            List<InvitePlanMemberDto> invitePlanMemberDtoList = new ArrayList<>();
            List<InvitePlanMember> invitePlanMemberList = invitePlan.getInviteFriendList();
            for (InvitePlanMember planMember : invitePlanMemberList) {

                InvitePlanMemberDto invitePlanMemberDto = InvitePlanMemberDto.of(planMember.getMember());
                invitePlanMemberDtoList.add(invitePlanMemberDto);
            }

            invitePlanDto.setInvitePlanMemberDtoList(invitePlanMemberDtoList);

            List<LocationDto> locationDtoList = new ArrayList<>();
            List<Location> locationList = invitePlan.getLocationList();
            for (Location location : locationList) {

                LocationDto locationDto = LocationDto.of(location);
                locationDtoList.add(locationDto);
            }

            invitePlanDto.setLocationDtoList(locationDtoList);

            invitePlanDtoList.add(invitePlanDto);
        }

        response.setInvitePlanDtoList(invitePlanDtoList);

        return response;
    }

    @Override
    public Long accept(AcceptInvitedPlanRequest request, String email) {

//        1. Accept 상태로 바꾸기 (언제 삭제를 할까 ? 바로 or 일정 기간 후)
        InvitePlanMember invitePlanMember = invitePlanMemberRepository.getByIdAndEmail(Long.valueOf(request.getPlanId()), email);
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
