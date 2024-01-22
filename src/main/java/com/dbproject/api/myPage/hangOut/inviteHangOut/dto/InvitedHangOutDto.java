package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvitedHangOutDto {

    //보낸사람 주소
    private String requesterEmail;

    //보낸 사람 이름
    private String requesterName;

    //메시지
    private String message;

    //약속 날짜
    private LocalDateTime departDateTime;

    //사진
    private String picture1;

    //장소 이름
    private String name;

    public InvitedHangOutDto() {
    }


    public InvitedHangOutDto(String requesterEmail, String requesterName, String message, LocalDateTime departDateTime, String picture1, String name) {
        this.requesterEmail = requesterEmail;
        this.requesterName = requesterName;
        this.message = message;
        this.departDateTime = departDateTime;
        this.picture1 = picture1;
        this.name = name;
    }

}
