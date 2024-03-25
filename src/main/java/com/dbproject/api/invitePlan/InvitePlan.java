package com.dbproject.api.invitePlan;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.location.Location;
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
public class InvitePlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="invite_plan_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "period")
    private PlanPeriod period;

//    아직 누가 할지 정해지지 않은 준비물
    private String supply;      //나중에 분배 가능하게 변경하기

    @Column(name = "depart_date")
    private LocalDate departDate;

    @Column(name = "arrive_date")
    private LocalDate arriveDate;

    @OneToMany(mappedBy = "invitePlan")
//    @JoinColumn(name = )
    private List<InvitePlanMember> InviteFriendList;        //다대다 양방향, 외래키는 many 쪽에 있으므로 여기는 mappedby

    @OneToMany(mappedBy = "invitePlan")
    private List<Location> locationList;

//    @OneToMany(mappedBy = "plan")
//    private List<PlanLocation> locationList;

    public InvitePlan() {
    }

    @Builder
    public InvitePlan(String name, PlanPeriod period, String supply, LocalDate departDate, LocalDate arriveDate) {
        this.name = name;
        this.period = period;
        this.supply = supply;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
    }

    public static InvitePlan createInvitePlan(InvitePlanRequest request) {

        return InvitePlan.builder()
                .name(request.getName())
                .period(request.getPlanPeriod())
                .supply(request.getSupply())
                .departDate(request.getDepartDate())
                .arriveDate(request.getArriveDate())
                .build();
    }

    public static InvitePlan createInvitePlanWithSetList(InvitePlanRequest request) {

        return InvitePlan.builder()
                .name(request.getName())
                .period(request.getPlanPeriod())
                .supply(request.getSupply())
                .departDate(request.getDepartDate())
                .arriveDate(request.getArriveDate())
                .build();
    }
}
