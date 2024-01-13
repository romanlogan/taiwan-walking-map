package com.dbproject.web.friend;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcceptAddFriendRequest {

    private String friendRequestId;

    public AcceptAddFriendRequest(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }
}
