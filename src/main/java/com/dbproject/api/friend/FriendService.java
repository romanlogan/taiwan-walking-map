package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.exception.DuplicateFriendRequestException;
import com.dbproject.web.friend.AcceptAddFriendRequest;
import com.dbproject.web.friend.AddFriendRequest;
import com.dbproject.web.friend.RejectFriendRequest;
import com.dbproject.web.friend.RequestFriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    private final FriendRequestRepository friendRequestRepository;


    public Long saveFriendRequest(AddFriendRequest addFriendRequest, String requesterEmail) {

        //요청자 Member 찾기
        Member requester = memberRepository.findByEmail(requesterEmail);

        //응답자 Member 찾기
        Member respondent = memberRepository.findByEmail(addFriendRequest.getFriendEmail());

        checkValidateFriendRequest(requester, respondent);

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent,addFriendRequest.getMemo());
        friendRequestRepository.save(friendRequest);

        return 1L;
    }

    private void checkValidateFriendRequest(Member requester, Member respondent) {
        FriendRequest savedFriendRequest = friendRequestRepository.findByRequesterAndRespondent(requester, respondent);
        if (savedFriendRequest != null) {
            throw new DuplicateFriendRequestException("이미 요청한 사용자 입니다.");
        }
    }

    public Page<RequestFriendListDto> getRequestFriendList(Pageable pageable, String email) {

        return friendRequestRepository.getRequestFriendListPage(pageable,email);
    }


    public Long acceptAddFriend(AcceptAddFriendRequest acceptAddFriendRequest,String email) {

        FriendRequest friendRequest = getFriendRequest(acceptAddFriendRequest.getFriendRequestId(),FriendRequestStatus.ACCEPTED);

        Member requester =  friendRequest.getRequester();
        Member respondent = friendRequest.getRespondent();


        Friend friend = Friend.createFriend(requester,respondent);
        Friend reverseFriend = Friend.createFriend(respondent, requester);

        friendRepository.save(friend);
        friendRepository.save(reverseFriend);

        return friend.getId();
    }

    private FriendRequest getFriendRequest(Long FriendRequestId, FriendRequestStatus status) {

        FriendRequest friendRequest = friendRequestRepository.findById(FriendRequestId).orElseThrow(EntityNotFoundException::new);
        friendRequest.setFriendRequestStatus(status);
        return friendRequest;
    }


    public void rejectFriendRequest(RejectFriendRequest deleteFriendRequest) {
        //변경 감지가 동작 하나 ? -> yes

        getFriendRequest(deleteFriendRequest.getFriendRequestId(), FriendRequestStatus.REJECTED);
//        friendRequestRepository.deleteById(Long.valueOf(deleteFriendRequest.getFriendRequestId()));
    }

//    public Page<FriendListResponse> getFriendList(Pageable pageable, String email) {
//
//        return
//    }
}
