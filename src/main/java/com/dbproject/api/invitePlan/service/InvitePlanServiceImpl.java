package com.dbproject.api.invitePlan.service;

import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.invitePlanMember.repository.InvitePlanMemberRepository;
import com.dbproject.api.invitePlan.repository.InvitePlanRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class InvitePlanServiceImpl implements InvitePlanService {

    private final InvitePlanRepository invitePlanRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;
    private final InvitePlanMemberRepository invitePlanMemberRepository;

    public void setLocationList(InvitePlan savedInvitePlan, InvitePlanRequest request) {
        List<Location> locationList = new ArrayList<>();
        for (InvitePlanLocationRequest locationRequest : request.getInvitePlanLocationRequestList()) {
            Location location = locationRepository.findByLocationId(locationRequest.getLocationId());       //savedInvitePlan 이 id 가 0인데 이 아이디가 0인 savedInvitePlan 을 찾아르 수 없다
            locationList.add(location);
        }
        savedInvitePlan.setLocationList(locationList);
    }

    public void setInvitePlanMemberList(InvitePlan savedInvitePlan, InvitePlanRequest request){
        List<InvitePlanMember> invitePlanMemberList = new ArrayList<>();
        for (InvitePlanMemberRequest memberRequest: request.getInvitePlanMemberRequestList()) {
            //0.Member 찾기

            String email = memberRequest.getFriendEmail();
            Member member = memberRepository.findByEmail(email);

            //1. InvitePlanMember 생성 및 저장
            InvitePlanMember invitePlanMember = InvitePlanMember.createInvitePlanMember(memberRequest.getFriendSupply(), member, savedInvitePlan);
            invitePlanMemberRepository.save(invitePlanMember);

            //2. InvitePlanMemberList 에 InvitePlanMember  추가
            invitePlanMemberList.add(invitePlanMember);
        }
        savedInvitePlan.setInviteFriendList(invitePlanMemberList);

    }

    public Long invitePlan(InvitePlanRequest request) {

        //0. InvitePlan 생성
        InvitePlan invitePlan = InvitePlan.createInvitePlan(request);
        //1.InvitePlan 저장
        InvitePlan savedInvitePlan = invitePlanRepository.save(invitePlan);

        //2. locationList 생성 및 InvitePlan 에 저장
        setLocationList(savedInvitePlan, request);

        //3. InvitePlanMemberList 생성 및 InvitePlan 에 저장
        setInvitePlanMemberList(savedInvitePlan,request);

        return savedInvitePlan.getId();
    }

}
