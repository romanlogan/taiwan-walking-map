package com.dbproject.api.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InviteHangOutFromLocRequest {

    @NotBlank
    private String locationId;

    @NotBlank
    private String friendEmail;

    @NotNull
    private LocalDateTime departDateTime;

    private String message;

    public InviteHangOutFromLocRequest(String locationId, String friendEmail, LocalDateTime departDateTime, String message) {
        this.locationId = locationId;
        this.friendEmail = friendEmail;
        this.departDateTime = departDateTime;
        this.message = message;
    }
}
