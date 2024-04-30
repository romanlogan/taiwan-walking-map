package com.dbproject.api.plan;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.route.Route;
import com.dbproject.constant.PlanPeriod;
import lombok.Builder;
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
    private String region;
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


    public Plan() {
    }

    @Builder
    public Plan(String name,String region, PlanPeriod period, String supply, Member requester, LocalDate departDate, LocalDate arriveDate, List<Route> route) {
        this.name = name;
        this.region = region;
        this.period = period;
        this.supply = supply;
        this.requester = requester;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
        this.route = route;
    }

    public static Plan createPlan(InvitePlan invitePlan) {

        return Plan.builder()
                .name(invitePlan.getName())
                .region(invitePlan.getRegion())
                .period(invitePlan.getPeriod())
                .supply(invitePlan.getSupply())
                .requester(invitePlan.getRequester())
                .departDate(invitePlan.getDepartDate())
                .arriveDate(invitePlan.getArriveDate())
                .route(invitePlan.getRouteList())
                .build();

    }
}
