package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.constant.FriendRequestStatus;
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



    public Long request(AddFriendRequest addFriendRequest, String requesterEmail) {

        //요청자 Member 찾기
        Member requester = memberRepository.findByEmail(requesterEmail);

        //응답자 Member 찾기
        String respondentEmail = addFriendRequest.getFriendEmail();
        Member respondent = memberRepository.findByEmail(respondentEmail);

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent,addFriendRequest.getMemo());
        friendRequestRepository.save(friendRequest);

        return 1L;
    }

    public Page<RequestFriendListDto> getRequestFriendList(Pageable pageable, String email) {

        return friendRequestRepository.getRequestFriendListPage(pageable,email);
    }


    public Long acceptAddFriend(AcceptAddFriendRequest acceptAddFriendRequest) {

        //FriendRequest 엔티티에서 두 엔티티를 꺼내온다
        Long FriendRequestId = Long.valueOf(acceptAddFriendRequest.getFriendRequestId());
        FriendRequest friendRequest = getFriendRequest(FriendRequestId,FriendRequestStatus.ACCEPTED);

        Member requester = friendRequest.getRequester();
        Member respondent = friendRequest.getRespondent();

        //두 엔티티를 친구로 등록한다
        Friend friend = Friend.createFriend(requester, respondent);
        friendRepository.save(friend);

        //FriendRequest 에서 이 친구 요청을 삭제한다 (이미 수락됨) -> 삭제가 아니라 요청 수락됨으로 바꾸고 삭제 는 다른 버튼으로 처리
//        friendRequestRepository.delete(friendRequest);
        return friend.getId();
    }

    private FriendRequest getFriendRequest(Long FriendRequestId, FriendRequestStatus status) {

        FriendRequest friendRequest = friendRequestRepository.findById(FriendRequestId).orElseThrow(EntityNotFoundException::new);
        friendRequest.setFriendRequestStatus(status);
        return friendRequest;
    }


    public void rejectFriendRequest(RejectFriendRequest deleteFriendRequest) {

        Long friendRequestId = Long.valueOf(deleteFriendRequest.getFriendRequestId());
        //변경 감지가 동작 하나 ?
        getFriendRequest(friendRequestId, FriendRequestStatus.REJECTED);
//        friendRequestRepository.deleteById(Long.valueOf(deleteFriendRequest.getFriendRequestId()));
    }
}
