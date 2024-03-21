package com.dbproject.api.invitePlan.invitePlanMember;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.member.Member;
import com.dbproject.constant.InvitePlanStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "invite_plan_member")
@Getter
@Setter
public class InvitePlanMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invite_plan_member_id")
    private Long id;

    private String supply;      //누가 가져가야할지 정해진 준비

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private InvitePlanStatus invitePlanStatus;

    @ManyToOne(fetch = FetchType.LAZY)      //plan 과 매핑
    @JoinColumn(name = "invite_plan_id")
    private InvitePlan invitePlan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member;

    public InvitePlanMember() {
    }

    @Builder
    public InvitePlanMember(String supply, InvitePlanStatus invitePlanStatus, InvitePlan invitePlan, Member member) {
        this.supply = supply;
        this.invitePlanStatus = invitePlanStatus;
        this.invitePlan = invitePlan;
        this.member = member;
    }

    public static InvitePlanMember createInvitePlanMember(String supply, Member member, InvitePlan invitePlan) {

        return InvitePlanMember.builder()
                .supply(supply)
                .invitePlanStatus(InvitePlanStatus.WAITING)
                .invitePlan(invitePlan)
                .member(member)
                .build();
    }
}
