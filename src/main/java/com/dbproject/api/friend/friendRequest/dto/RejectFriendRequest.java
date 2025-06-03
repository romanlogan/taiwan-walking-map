package com.dbproject.api.friend.friendRequest.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RejectFriendRequest {

    @NotNull(message = "friendRequestId value is required")
    private Long friendRequestId;

    public RejectFriendRequest() {
    }

    public RejectFriendRequest(Long friendRequestId) {
        this.friendRequestId = friendRequestId;
    }
}
