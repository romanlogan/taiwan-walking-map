package com.dbproject.api.plan;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.route.Route;
import com.dbproject.constant.PlanPeriod;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
public class Plan extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="plan_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private PlanPeriod period;

    //    아직 누가 할지 정해지지 않은 준비물
    private String supply;      //나중에 분배 가능하게 변경하기

    @OneToOne
    @JoinColumn(name = "member_email")
    private Member requester;

    @Column(name = "depart_date")
    private LocalDate departDate;

    @Column(name = "arrive_date")
    private LocalDate arriveDate;

    @OneToMany(mappedBy = "plan")
//    @JoinColumn(name = )
    private List<PlanMember> planMemberList;        //다대다 양방향, 외래키는 many 쪽에 있으므로 여기는 mappedby

//    1일차 , 2일차 등등이 있으므로 List
    @OneToMany(mappedBy = "plan")
    private List<Route> route;




    public static Plan createPlan(InvitePlan invitePlan) {

//        어떻게 invitePlan 의 LocationList 를 route 로 바꿀것인가
//        1. invitePlan 의 locationList를 아에 Route 로 변경
//        2. loop 돌리기로 될까

    }
}
