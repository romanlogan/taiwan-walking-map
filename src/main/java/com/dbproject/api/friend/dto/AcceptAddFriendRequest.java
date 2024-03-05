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

//    Integer 로 바꾸면 자동으로 형변환을 해주는거였나?
    @NotNull
    private Long friendRequestId;

    public AcceptAddFriendRequest(Long friendRequestId) {
        this.friendRequestId = friendRequestId;
    }
}
