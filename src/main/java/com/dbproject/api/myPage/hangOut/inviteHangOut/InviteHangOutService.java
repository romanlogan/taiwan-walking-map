package com.dbproject.api.myPage.hangOut.inviteHangOut;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.InviteHangOutLocationDto;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.InvitedHangOutDto;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.InvitedHangOutResponse;
import com.dbproject.constant.InviteHangOutStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class InviteHangOutService {

    private final InviteHangOutRepository inviteHangOutRepository;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;


    //초대 하기
    public Long inviteHangOut(InviteHangOutRequest inviteHangOutRequest, String email) {

        //프록시 객체로 가져오므로 location 정보가 없어서 location 정보가 필요할때 쿼리를 한번 더 날리게 될것
        //entity not found 에러 처리 문제 ?
        FavoriteLocation favoriteLocation = favoriteRepository.findById(inviteHangOutRequest.getFavoriteLocationId()).orElseThrow(EntityNotFoundException::new);
        Member me = memberRepository.findByEmail(email);
        Member friend = memberRepository.findByEmail(inviteHangOutRequest.getFriendEmail());

        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(inviteHangOutRequest.getMessage(),inviteHangOutRequest.getDepartDateTime(), favoriteLocation, me, friend, InviteHangOutStatus.WAITING);
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

}
















