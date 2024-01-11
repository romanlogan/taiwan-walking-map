package com.dbproject.web.friend;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFriendRequest {

    private String friendEmail;
    private String memo;

    public AddFriendRequest() {
    }

    public AddFriendRequest(String friendEmail, String memo) {
        this.friendEmail = friendEmail;
        this.memo = memo;
    }
}
