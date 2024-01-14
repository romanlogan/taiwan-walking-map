package com.dbproject.web.friend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddFriendRequest {

    @NotEmpty
    private String friendEmail;

    private String memo;

    public AddFriendRequest() {
    }

    public AddFriendRequest(String friendEmail, String memo) {
        this.friendEmail = friendEmail;
        this.memo = memo;
    }
}
