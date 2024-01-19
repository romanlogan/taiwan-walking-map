package com.dbproject.api.friend;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddFriendRequest {

    @NotBlank
    @Email(message = "이메일 형식이 아닙니다.")
    private String friendEmail;

    private String memo;

    public AddFriendRequest() {
    }

    public AddFriendRequest(String friendEmail, String memo) {
        this.friendEmail = friendEmail;
        this.memo = memo;
    }
}
