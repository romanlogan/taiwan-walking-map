package com.dbproject.web.friend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RejectFriendRequest {

    private String friendRequestId;


    public RejectFriendRequest() {
    }

    public RejectFriendRequest(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }
}
