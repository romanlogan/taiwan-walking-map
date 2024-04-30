package com.dbproject.api.route;


import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.InvitePlanRouteRequest;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.plan.Plan;
import com.dbproject.api.plan.PlanMember;
import com.dbproject.api.routeLocation.RouteLocation;
import com.dbproject.constant.RouteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "route")
@Getter
@Setter
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long id;

//    private String region;
//    private String startPoint;
//    private String wayPoint1;
//    private String wayPoint2;
//    private String arrivePoint;

//    몇일차 루트인가
    @Column(name = "days")
    private Integer day;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RouteStatus routeStatus;

    //default null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;    //plan 추가

    //default null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invite_plan_id")
    private InvitePlan invitePlan;

    @OneToMany(mappedBy = "route")
    private List<RouteLocation> routeLocationList;        //다대다 양방향, 외래키는 many 쪽에 있으므로 여기는 mappedby


    public Route() {
    }

    @Builder
    public Route(Integer day, RouteStatus routeStatus) {
        this.day = day;
        this.routeStatus = routeStatus;
    }

    public static Route createRoute(InvitePlanRouteRequest routeRequest) {

        return Route.builder()
                .day(routeRequest.getDay())
                .routeStatus(RouteStatus.INVITEPLAN)
                .build();
    }


}
