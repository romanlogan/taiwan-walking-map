package com.dbproject.api.myPage.hangOut.hangOut;


import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOut;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hang_out")
@Getter
@Setter
public class HangOut extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hang_out_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_email",
            referencedColumnName = "member_email")
    private Member requester;

//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "respondent_email",
            referencedColumnName = "member_email")
    private Member respondent;

    private String message;

    @Column(name = "depart_date_time")
    private LocalDateTime departDateTime;

    public HangOut() {
    }

    @Builder
    public HangOut(Location location, Member requester, Member respondent, String message, LocalDateTime departDateTime) {
        this.location = location;
        this.requester = requester;
        this.respondent = respondent;
        this.message = message;
        this.departDateTime = departDateTime;
    }

    public static HangOut createByInvitedHangOut(InviteHangOut inviteHangOut) {

        return HangOut.builder()
                .location(inviteHangOut.getLocation())
                .requester(inviteHangOut.getRequester())
                .respondent(inviteHangOut.getRespondent())
                .message(inviteHangOut.getMessage())
                .departDateTime(inviteHangOut.getDepartDateTime())
                .build();
    }

    public static HangOut createByInvitedHangOutReverse(InviteHangOut inviteHangOut) {

        return HangOut.builder()
                .location(inviteHangOut.getLocation())
                .requester(inviteHangOut.getRespondent())
                .respondent(inviteHangOut.getRequester())
                .message(inviteHangOut.getMessage())
                .departDateTime(inviteHangOut.getDepartDateTime())
                .build();
    }

}



