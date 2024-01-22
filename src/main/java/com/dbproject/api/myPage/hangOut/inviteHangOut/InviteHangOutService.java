package com.dbproject.api.myPage.hangOut.inviteHangOut;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
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

@Service
@Transactional
@RequiredArgsConstructor
public class InviteHangOutService {

    private final InviteHangOutRepository inviteHangOutRepository;
    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;


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


    public InvitedHangOutResponse getInvitedHangOutList(String email) {

        List<InviteHangOut> invitedHangOutList = inviteHangOutRepository.findWaitingListByRequesterEmail(email);

        List<InvitedHangOutDto> invitedHangOutDtoList = new ArrayList<>();

        for (InviteHangOut invitedHangOut : invitedHangOutList) {

            //여기 성능 장애 의심됨 fetch join 으로 바꾸기
            String requesterName = invitedHangOut.getRequester().getName();
            String requesterEmail = invitedHangOut.getRequester().getEmail();
            String message = invitedHangOut.getMessage();
            LocalDateTime departDateTime = invitedHangOut.getDepartDateTime();
            String picture1Url = invitedHangOut.getFavoriteLocation().getLocation().getLocationPicture().getPicture1();
            String locationName = invitedHangOut.getFavoriteLocation().getLocation().getName();

            InvitedHangOutDto invitedHangOutDto = new InvitedHangOutDto(requesterName,requesterEmail,message,departDateTime,picture1Url,locationName);
            invitedHangOutDtoList.add(invitedHangOutDto);
        }

        InvitedHangOutResponse invitedHangOutResponse = new InvitedHangOutResponse();


    }
}
















