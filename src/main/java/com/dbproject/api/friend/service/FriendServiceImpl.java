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
        checkHasBeenRequested(requester, respondent);           // Check that, i have already sent a friend request to this respondent

//        need add situation 2 code (reference test code)

        FriendRequest savedRequest = createAndSaveFriendRequest(addFriendRequest, requester, respondent);
        return savedRequest.getId();
    }

    private FriendRequest createAndSaveFriendRequest(AddFriendRequest addFriendRequest, Member requester, Member respondent) {

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, addFriendRequest.getMemo());
        return friendRequestRepository.save(friendRequest);
    }

    private static void checkRespondentExist(Member respondent) {
        if (respondent == null) {
            throw new MemberNotExistException("user does not exist.");
        }
    }

    private void checkHasBeenRequested(Member requester, Member respondent) {

        FriendRequest savedFriendRequest = friendRequestRepository.findByRequesterAndRespondent(requester, respondent);
        if (savedFriendRequest != null) {
            throw new DuplicateFriendRequestException("This user has already sent a friend request.");
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
        createAndSaveFriend(respondent, requester);     // save reverse friend relation

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
        friendRequest.changeStatus(status);
        return friendRequest;
    }

    private static void checkFriendRequestExist(Optional<FriendRequest> optionalFriendRequest) {
        if (optionalFriendRequest.isEmpty()) {
            throw new FriendRequestNotExistException("This friend request does not exist");
        }
    }


    public void rejectFriendRequest(RejectFriendRequest deleteFriendRequest) {

//        Change to REJECT status and do not delete
        getChangedStatusFriendRequest(deleteFriendRequest.getFriendRequestId(), FriendRequestStatus.REJECTED);
    }

// get my friend list
    public FriendListResponse getFriendList(String email) {

        List<FriendDto> friendDtoList = getFriendDtoList(email);

//        get favorite list for hang up feature (send hang up request to friend)
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

//       predict to N+1 problem point (query to search memberImg table for each friend)
        List<Friend> friendList = friendRepository.getFriendList(email);
        for (Friend friend : friendList) {
            friendDtoList.add(createFriendDtoFrom(friend));
        }
    }

    private void checkIsKnownUser(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new MemberNotExistException("This member does not exist.");
        }
    }

    private FriendDto createFriendDtoFrom(Friend friend) {

        Optional<MemberImg> friendImg = memberImgRepository.findByMemberEmail(friend.getNewFriend().getEmail());
        return FriendDto.from(friend, friendImg);
    }

    private List<FavoriteLocationDto> getFavoriteLocationDtoList(String email) {

        List<FavoriteLocationDto> favoriteLocationDtoList = new ArrayList<>();
        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findFavoriteLocationListByEmail(email);

        // need refactoring to get dtos directly
        for (FavoriteLocation favoriteLocation : favoriteLocationList) {
            favoriteLocationDtoList.add(FavoriteLocationDto.of(favoriteLocation));
        }

        return favoriteLocationDtoList;
    }

}
