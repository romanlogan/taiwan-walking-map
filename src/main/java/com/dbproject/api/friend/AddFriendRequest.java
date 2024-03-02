package com.dbproject.api.friend;

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

    @NotBlank(message = "friendEmail值是必要")
    @Email(message = "請輸入email的形式")
    private String friendEmail;

    @Length(max = 255, message = "memo只能最多255字")
    private String memo;

    public AddFriendRequest() {
    }

    public AddFriendRequest(String friendEmail, String memo) {
        this.friendEmail = friendEmail;
        this.memo = memo;
    }
}
