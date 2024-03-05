package com.dbproject.api.hangOut.inviteHangOut.service;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.hangOut.hangOut.repository.HangOutRepository;
import com.dbproject.api.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.api.hangOut.inviteHangOut.dto.*;
import com.dbproject.api.hangOut.inviteHangOut.repository.InviteHangOutRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.hangOut.hangOut.HangOut;
import com.dbproject.constant.InviteHangOutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InviteHangOutServiceImpl implements InviteHangOutService {

    private final InviteHangOutRepository inviteHangOutRepository;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final HangOutRepository hangOutRepository;
    private final LocationRepository locationRepository;


    //초대 하기
    public Long inviteHangOut(InviteHangOutRequest inviteHangOutRequest, String email) {

        //프록시 객체로 가져오므로 location 정보가 없어서 location 정보가 필요할때 쿼리를 한번 더 날리게 될것
        //entity not found 에러 처리 문제 ?

        //favoriteLocation 말고 favoriteLocationId 로 장소를 찾아서
        // Location 으로 저장
        FavoriteLocation favoriteLocation = favoriteRepository.findById(inviteHangOutRequest.getFavoriteLocationId()).orElseThrow(EntityNotFoundException::new);
        Member me = memberRepository.findByEmail(email);
        Member friend = memberRepository.findByEmail(inviteHangOutRequest.getFriendEmail());

        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(inviteHangOutRequest.getMessage(),inviteHangOutRequest.getDepartDateTime(), favoriteLocation.getLocation(), me, friend, InviteHangOutStatus.WAITING);
        Long id = inviteHangOutRepository.save(inviteHangOut).getId();

        return id;
    }


    //초대 받은 목록 가져오기
    public InvitedHangOutResponse getInvitedHangOutList(String email, Optional<Long> optionalInviteHangOutId) {

        List<InvitedHangOutDto> invitedHangOutDtoList = getInvitedHangOutDtoList(email);
        InviteHangOutLocationDto inviteHangOutLocationDto = getInviteHangOutLocationDto(optionalInviteHangOutId);

        return new InvitedHangOutResponse(inviteHangOutLocationDto ,invitedHangOutDtoList);
    }


    private List<InvitedHangOutDto> getInvitedHangOutDtoList(String email) {

        // hangout 리스트를 가져온다
        List<InviteHangOut> invitedHangOutList = inviteHangOutRepository.findWaitingListByRequesterEmail(email);
        List<InvitedHangOutDto> invitedHangOutDtoList = new ArrayList<>();

        //dto 로 변환 (스트림으로 변환하기)
        for (InviteHangOut invitedHangOut : invitedHangOutList) {

            //여기 성능 장애 의심됨 fetch join 으로 바꾸기
            InvitedHangOutDto invitedHangOutDto = InvitedHangOutDto.from(invitedHangOut);
            invitedHangOutDtoList.add(invitedHangOutDto);
        }

        return invitedHangOutDtoList;
    }

    private InviteHangOutLocationDto getInviteHangOutLocationDto(Optional<Long> optionalInviteHangOutId) {

        InviteHangOutLocationDto inviteHangOutLocationDto;

        if(optionalInviteHangOutId.isPresent()) {       // 초대받은 locationDtl 을 보고싶을때

            Long inviteHangOutId = optionalInviteHangOutId.get();
            InviteHangOut inviteHangOut = inviteHangOutRepository.findById(inviteHangOutId).orElseThrow(EntityNotFoundException::new);
            inviteHangOutLocationDto = InviteHangOutLocationDto.from(inviteHangOut);

        }else{      // 처음 들어와서 리스트만 보일때
            inviteHangOutLocationDto = InviteHangOutLocationDto.createEmptyDto();
        }
        return inviteHangOutLocationDto;
    }



    public void acceptInvitedHangOut(AcceptInvitedHangOutRequest acceptInvitedHangOutRequest) {

        //행아웃 요청 목록에서 지우고
        Optional<InviteHangOut> invitedHangOut = inviteHangOutRepository.findById(acceptInvitedHangOutRequest.getInviteHangOutId());
        inviteHangOutRepository.deleteById(acceptInvitedHangOutRequest.getInviteHangOutId());
        InviteHangOut inviteHangOut = invitedHangOut.get();

        //행아웃 을 새로 만들어 저장
        //저장후 나중에 스케쥴 뽑을때를 위해 requester respondent 바꾼것도 저장
        HangOut hangOut = HangOut.createByInvitedHangOut(inviteHangOut);
        HangOut reverseHangOut = HangOut.createByInvitedHangOutReverse(inviteHangOut);
        hangOutRepository.save(hangOut);
        hangOutRepository.save(reverseHangOut);

    }

    public void rejectInvitedHangOut(RejectInvitedHangOutRequest rejectInvitedHangOutRequest) {

//        거절시 요청을 바로 삭제하기
//        inviteHangOutRepository.deleteById(Long.valueOf(rejectInvitedHangOutRequest.getInviteHangOutId()));

        //거절시 요청을 거절 상태로 변환
//        나중에 요청자가 거절됨을 확인 하기위해
        Optional<InviteHangOut> savedInviteHangOut = inviteHangOutRepository.findById(rejectInvitedHangOutRequest.getInviteHangOutId());
        InviteHangOut inviteHangOut = savedInviteHangOut.get();
        inviteHangOut.rejectInvitedHangOut();

    }

    public void inviteFromLocationPage(InviteHangOutFromLocRequest inviteHangOutFromLocRequest, String email) {

        //1. InviteHangOut 생성
        Location location = locationRepository.findByLocationId(inviteHangOutFromLocRequest.getLocationId());
        Member requester = memberRepository.findByEmail(email);
        Member respondent = memberRepository.findByEmail(inviteHangOutFromLocRequest.getFriendEmail());
        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(inviteHangOutFromLocRequest.getMessage(), inviteHangOutFromLocRequest.getDepartDateTime(), location, requester, respondent, InviteHangOutStatus.WAITING);

        //2. InviteHangOut 저장
        inviteHangOutRepository.save(inviteHangOut);
    }
}
















