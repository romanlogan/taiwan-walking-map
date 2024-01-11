package com.dbproject.api.friend;

import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.web.friend.AddFriendRequest;
import com.dbproject.web.friend.RequestFriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;


    private final FriendRequestRepository friendRequestRepository;

    public Long request(AddFriendRequest addFriendRequest,String email) {

        //요청자 Member 찾기
        String requesterEmail = email;
        Member requester = memberRepository.findByEmail(requesterEmail);

        //응답자 Member 찾기
        String respondentEmail = addFriendRequest.getFriendEmail();
        Member respondent = memberRepository.findByEmail(respondentEmail);

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent,addFriendRequest.getMemo());
        friendRequestRepository.save(friendRequest);

        return requester.getId();
    }

    public Page<RequestFriendListDto> getRequestFriendList(Pageable pageable, String email) {

        return friendRequestRepository.getRequestFriendListPage(pageable,email);
    }


}
