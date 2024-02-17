package com.dbproject.api.myPage.hangOut.inviteHangOut;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class InviteHangOutRequest {

    @NotNull
    private Long favoriteLocationId;

    @NotNull
    private LocalDateTime departDateTime;

    private String message;

    @NotBlank
    private String friendEmail;

    public InviteHangOutRequest(Long favoriteLocationId, LocalDateTime departDateTime, String message, String friendEmail) {
        this.favoriteLocationId = favoriteLocationId;
        this.departDateTime = departDateTime;
        this.message = message;
        this.friendEmail = friendEmail;
    }
}
