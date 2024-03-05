package com.dbproject.api.friend.service;

import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.friend.dto.AcceptAddFriendRequest;
import com.dbproject.api.friend.dto.AddFriendRequest;
import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.dto.RejectFriendRequest;
import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import com.dbproject.constant.FriendRequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FriendService {

    Long saveFriendRequest(AddFriendRequest addFriendRequest, String requesterEmail);
    Page<RequestFriendListDto> getRequestFriendList(Pageable pageable, String email);
    Long acceptAddFriend(AcceptAddFriendRequest acceptAddFriendRequest);
    void rejectFriendRequest(RejectFriendRequest deleteFriendRequest);
    FriendListResponse getFriendList(String email);

}
