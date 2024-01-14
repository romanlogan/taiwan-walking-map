package com.dbproject.web.friend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RejectFriendRequest {

    @NotEmpty
    private String friendRequestId;


    public RejectFriendRequest() {
    }

    public RejectFriendRequest(String friendRequestId) {
        this.friendRequestId = friendRequestId;
    }
}
