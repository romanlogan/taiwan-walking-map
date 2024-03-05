package com.dbproject.api.friend.friendRequest.repository;

import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRequestRepositoryCustom {

    public Page<RequestFriendListDto> getRequestFriendListPage(Pageable pageable, String email);
}
