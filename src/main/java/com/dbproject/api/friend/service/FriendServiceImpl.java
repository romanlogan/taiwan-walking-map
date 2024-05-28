package com.dbproject.api.friend.service;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.friend.*;
import com.dbproject.api.friend.dto.AcceptAddFriendRequest;
import com.dbproject.api.friend.dto.AddFriendRequest;
import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.repository.FriendRepository;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.repository.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.memberImg.MemberImg;
import com.dbproject.api.member.memberImg.MemberImgRepository;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.exception.DuplicateFriendRequestException;
import com.dbproject.api.friend.friendRequest.dto.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import com.dbproject.exception.FriendRequestNotExistException;
import com.dbproject.exception.MemberNotExistException;
import com.dbproject.exception.UnknownUserTryGetFriendListException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FriendServiceImpl implements FriendService{

    private final MemberRepository memberRepository;

    private final FriendRepository friendRepository;

    private final FriendRequestRepository friendRequestRepository;

    private final FavoriteRepository favoriteRepository;

    private final MemberImgRepository memberImgRepository;


    public Long saveFriendRequest(AddFriendRequest addFriendRequest, String requesterEmail) {

        Member requester = memberRepository.findByEmail(requesterEmail);
        Member respondent = memberRepository.findByEmail(addFriendRequest.getFriendEmail());

        checkRespondentExist(respondent);
        checkHasBeenRequested(requester, respondent);

        FriendRequest savedRequest = createAndSaveFriendRequest(addFriendRequest, requester, respondent);
        return savedRequest.getId();
    }

    private FriendRequest createAndSaveFriendRequest(AddFriendRequest addFriendRequest, Member requester, Member respondent) {

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, addFriendRequest.getMemo());

        return friendRequestRepository.save(friendRequest);
    }

    private static void checkRespondentExist(Member respondent) {
        if (respondent == null) {
            throw new MemberNotExistException("존재하지 않는 유저 입니다.");
        }
    }


    private void checkHasBeenRequested(Member requester, Member respondent) {
        FriendRequest savedFriendRequest = friendRequestRepository.findByRequesterAndRespondent(requester, respondent);
        if (savedFriendRequest != null) {
            throw new DuplicateFriendRequestException("이미 친구 요청한 사용자 입니다.");
        }
    }

    public Page<RequestFriendListDto> getRequestFriendList(Pageable pageable, String email) {

        return friendRequestRepository.getRequestFriendListPage(pageable,email);
    }


    public Long acceptAddFriend(AcceptAddFriendRequest acceptAddFriendRequest) {

        FriendRequest friendRequest = getChangedStatusFriendRequest(acceptAddFriendRequest.getFriendRequestId(),FriendRequestStatus.ACCEPTED);

        Member requester =  friendRequest.getRequester();
        Member respondent = friendRequest.getRespondent();

        Friend friend = createAndSaveFriend(requester, respondent);
        createAndSaveFriend(respondent, requester);     //reverse

        return friend.getId();
    }

    private Friend createAndSaveFriend(Member requester, Member respondent) {
        Friend friend = Friend.createFriend(requester, respondent);
        friendRepository.save(friend);
        return friend;
    }

    private FriendRequest getChangedStatusFriendRequest(Long FriendRequestId, FriendRequestStatus status) {

        Optional<FriendRequest> optionalFriendRequest = friendRequestRepository.findById(FriendRequestId);

        checkFriendRequestExist(optionalFriendRequest);

        FriendRequest friendRequest = optionalFriendRequest.get();
        friendRequest.changeStatusTo(status);

        return friendRequest;
    }

    private static void checkFriendRequestExist(Optional<FriendRequest> optionalFriendRequest) {
        if (optionalFriendRequest.isEmpty()) {
            throw new FriendRequestNotExistException("존재하지 않는 친구 요청 입니다.");
        }
    }


    public void rejectFriendRequest(RejectFriendRequest deleteFriendRequest) {
        //변경 감지가 동작 하나 ? -> yes

//        reject 로 변경하고 삭제X
        getChangedStatusFriendRequest(deleteFriendRequest.getFriendRequestId(), FriendRequestStatus.REJECTED);
    }

    public FriendListResponse getFriendList(String email) {

        List<FriendDto> friendDtoList = getFriendDtoList(email);
        List<FavoriteLocationDto> favoriteLocationDtoList = getFavoriteLocationDtoList(email);

        return FriendListResponse.create(friendDtoList, favoriteLocationDtoList);
    }

    private List<FriendDto> getFriendDtoList(String email) {

        List<FriendDto> friendDtoList = new ArrayList<>();
        createAndAddFriendDtoTo(friendDtoList, email);

        return friendDtoList;
    }

    private void createAndAddFriendDtoTo(List<FriendDto> friendDtoList, String email) {

        checkIsKnownUser(email);
        //N+1 성능 장애 예상 지점(친구 1명마다 memberImg table 를 뒤져서 찾는 쿼리)
        List<Friend> friendList = friendRepository.getFriendList(email);
        for (Friend friend : friendList) {
            friendDtoList.add(createFriendDtoFrom(friend));
        }
    }

    private void checkIsKnownUser(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new UnknownUserTryGetFriendListException("존재하지 않는 유저는 친구 목록을 가져올 수 없습니다.");
        }
    }

    private FriendDto createFriendDtoFrom(Friend friend) {

        Optional<MemberImg> friendImg = memberImgRepository.findByMemberEmail(friend.getNewFriend().getEmail());
        return FriendDto.from(friend, friendImg);
    }

    private List<FavoriteLocationDto> getFavoriteLocationDtoList(String email) {

        List<FavoriteLocationDto> favoriteLocationDtoList = new ArrayList<>();
        createAndAddFavoriteLocationDtoTo(favoriteLocationDtoList, email);

        return favoriteLocationDtoList;
    }

    private void createAndAddFavoriteLocationDtoTo(List<FavoriteLocationDto> favoriteLocationDtoList, String email) {

        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findFavoriteLocationListByEmail(email);


        for (FavoriteLocation favoriteLocation : favoriteLocationList) {
            favoriteLocationDtoList.add(FavoriteLocationDto.of(favoriteLocation));
        }
    }

}
