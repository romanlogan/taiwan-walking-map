package com.dbproject.api.hangOut.inviteHangOut;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.constant.InviteHangOutStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "invite_hang_out")
@Getter
@Setter
//가볍게 노는 계획
public class InviteHangOut extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_hang_out_id")
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

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InviteHangOutStatus inviteHangOutStatus;

    public InviteHangOut() {
    }

    @Builder
    public InviteHangOut(String message, LocalDateTime departDateTime, InviteHangOutStatus inviteHangOutStatus, Location location, Member requester, Member respondent) {
        this.message = message;
        this.departDateTime = departDateTime;
        this.inviteHangOutStatus = inviteHangOutStatus;
        this.location = location;
        this.requester = requester;
        this.respondent = respondent;
    }

    public static InviteHangOut createHangOut(String message, LocalDateTime departDateTime, Location location, Member requester, Member respondent, InviteHangOutStatus inviteHangOutStatus) {

        return InviteHangOut.builder()
                .message(message)
                .departDateTime(departDateTime)
                .inviteHangOutStatus(inviteHangOutStatus)
                .location(location)
                .requester(requester)
                .respondent(respondent)
                .build();
    }

    public void rejectInvitedHangOut() {

        this.inviteHangOutStatus = InviteHangOutStatus.REJECTED;
    }
}
