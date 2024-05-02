package com.dbproject.api.planMember;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.member.Member;
import com.dbproject.api.plan.Plan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class PlanMember extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_member_id")
    private Long id;

    @Column(name = "supply")
    private String supply;      //누가 가져가야할지 정해진 준비

    @ManyToOne(fetch = FetchType.LAZY)      //plan 과 매핑
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member;

    public PlanMember() {
    }

    @Builder
    public PlanMember(String supply, Plan plan, Member member) {
        this.supply = supply;
        this.plan = plan;
        this.member = member;
    }

    public static PlanMember createPlanMember(InvitePlanMember invitePlanMember,Plan plan) {

        return PlanMember.builder()
                .supply(invitePlanMember.getSupply())
                .plan(plan)
                .member(invitePlanMember.getMember())
                .build();
    }
}
