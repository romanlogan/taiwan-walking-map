package com.dbproject.api.friend;

import com.dbproject.web.friend.RequestFriendListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRequestRepositoryCustom {

    public Page<RequestFriendListDto> getRequestFriendListPage(Pageable pageable, String email);
}
