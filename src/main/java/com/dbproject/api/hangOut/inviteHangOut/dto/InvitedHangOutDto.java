package com.dbproject.api.hangOut.inviteHangOut.dto;


import com.dbproject.api.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.constant.InviteHangOutStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InvitedHangOutDto {

    private Long id;

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
    private String locationName;

    private InviteHangOutStatus inviteHangOutStatus;

    public InvitedHangOutDto() {
    }


    @Builder
    public InvitedHangOutDto(Long id,String requesterEmail, String requesterName, String message, LocalDateTime departDateTime, String picture1, String name, InviteHangOutStatus inviteHangOutStatus) {
        this.id = id;
        this.requesterEmail = requesterEmail;
        this.requesterName = requesterName;
        this.message = message;
        this.departDateTime = departDateTime;
        this.picture1 = picture1;
        this.locationName = name;
        this.inviteHangOutStatus = inviteHangOutStatus;
    }

    public static InvitedHangOutDto from(InviteHangOut invitedHangOut) {


        return InvitedHangOutDto.builder()
                .id(invitedHangOut.getId())
                .requesterEmail(invitedHangOut.getRequester().getEmail())
                .requesterName(invitedHangOut.getRequester().getName())
                .message(invitedHangOut.getMessage())
                .departDateTime(invitedHangOut.getDepartDateTime())
                .picture1(invitedHangOut.getLocation().getLocationPicture().getPicture1())
                .name(invitedHangOut.getLocation().getName())
                .inviteHangOutStatus(invitedHangOut.getInviteHangOutStatus())
                .build();
    }

}
