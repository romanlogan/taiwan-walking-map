package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class InviteHangOutFromLocRequest {

    private String locationId;
    private String friendEmail;
    private LocalDateTime departDateTime;
    private String message;

    public InviteHangOutFromLocRequest(String locationId, String friendEmail, LocalDateTime departDateTime, String message) {
        this.locationId = locationId;
        this.friendEmail = friendEmail;
        this.departDateTime = departDateTime;
        this.message = message;
    }
}
