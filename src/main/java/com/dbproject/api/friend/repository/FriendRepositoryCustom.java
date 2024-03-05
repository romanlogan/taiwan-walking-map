package com.dbproject.api.friend.repository;

import com.dbproject.api.friend.dto.FriendDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRepositoryCustom {

    public Page<FriendDto> getFriendListPage(Pageable pageable, String email);
}
