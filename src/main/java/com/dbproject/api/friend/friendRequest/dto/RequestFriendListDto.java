package com.dbproject.api.friend.friendRequest.dto;

import com.dbproject.constant.FriendRequestStatus;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestFriendListDto {

    private Long id;

    private String email;

    private String name;

    private FriendRequestStatus friendRequestStatus;

    public RequestFriendListDto() {

    }

    @QueryProjection
    public RequestFriendListDto(Long id, String email, String name, FriendRequestStatus friendRequestStatus) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.friendRequestStatus = friendRequestStatus;
    }
}
