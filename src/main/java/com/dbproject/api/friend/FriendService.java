package com.dbproject.api.friend;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteLocationDto;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.exception.DuplicateFriendRequestException;
import com.dbproject.api.friend.friendRequest.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.RequestFriendListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendService {

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    private final FriendRequestRepository friendRequestRepository;

    private final FavoriteRepository favoriteRepository;



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


    public Long acceptAddFriend(AcceptAddFriendRequest acceptAddFriendRequest) {

        FriendRequest friendRequest = getFriendRequest(acceptAddFriendRequest.getFriendRequestId(),FriendRequestStatus.ACCEPTED);

        Member requester =  friendRequest.getRequester();
        Member respondent = friendRequest.getRespondent();

        Friend friend = Friend.createFriend(requester,respondent);
        Friend reverseFriend = Friend.createFriend(respondent, requester);
        friendRepository.save(friend);
        friendRepository.save(reverseFriend);

        return friend.getId();
    }

    //친구요청을 찾아 친구 요청을 받은 status 로 변경하고 반환한다
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

    public FriendListResponse getFriendList(Pageable pageable, String email) {

            // 즐겨찾기 리스트를 같이 가져와야 하는데
    //        검색 쿼리를 두번 날릴까
    //        애초에 페이징쿼리 날릴떄는 즐겨찾기 리스트를 가져올수 없다

        Page<FriendDto> friendListPages = getFriendDtoPage(pageable, email);
        List<FavoriteLocationDto> favoriteLocationDtoList = getFavoriteLocationDtoList(email);

        return FriendListResponse.createFriendListResponse(friendListPages, favoriteLocationDtoList);
    }

    private List<FavoriteLocationDto> getFavoriteLocationDtoList(String email) {
        List<FavoriteLocationDto> favoriteLocationDtoList = new ArrayList<>();

        List<FavoriteLocation> favoriteLocationList = favoriteRepository.getFavoriteLocationList(email);

        for (FavoriteLocation favoriteLocation : favoriteLocationList) {
            FavoriteLocationDto favoriteLocationDto = FavoriteLocationDto.of(favoriteLocation);
            favoriteLocationDtoList.add(favoriteLocationDto);
        }

        return favoriteLocationDtoList;
    }

    private Page<FriendDto> getFriendDtoPage(Pageable pageable, String email) {

        return friendRepository.getFriendListPage(pageable, email);
    }
}
