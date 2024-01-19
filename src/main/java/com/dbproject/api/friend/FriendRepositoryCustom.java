package com.dbproject.api.friend;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FriendRepositoryCustom {

    public Page<FriendDto> getFriendListPage(Pageable pageable, String email);
}
