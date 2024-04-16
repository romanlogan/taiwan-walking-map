package com.dbproject.api.route;


import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.plan.Plan;
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

    @OneToMany(mappedBy = "route")
    private List<Location> locationList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;    //plan 추가


}
