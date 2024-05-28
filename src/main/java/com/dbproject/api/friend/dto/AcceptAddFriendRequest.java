package com.dbproject.api.friend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AcceptAddFriendRequest {

    @NotNull(message = "friendRequestId value is required")
    private Long friendRequestId;

    public AcceptAddFriendRequest(Long friendRequestId) {
        this.friendRequestId = friendRequestId;
    }
}
