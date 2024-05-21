package com.dbproject.api.friend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AddFriendRequest {

    @NotBlank(message = "friendEmail value is required")
    @Email(message = "please enter email format")
    private String friendEmail;

    @Length(max = 255, message = "memo can only have a maximum of 255 characters")
    private String memo;

    public AddFriendRequest() {
    }

    @Builder
    public AddFriendRequest(String friendEmail, String memo) {
        this.friendEmail = friendEmail;
        this.memo = memo;
    }

    public static AddFriendRequest create(String friendEmail, String memo) {

        return AddFriendRequest.builder()
                .friendEmail(friendEmail)
                .memo(memo)
                .build();
    }
}
