package com.dbproject.api.invitePlan;
//
//import com.dbproject.api.location.Location;
//import lombok.Getter;
//import lombok.Setter;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "plan_location")
//@Getter
//@Setter
//public class PlanLocation {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "plan_location_id")
//    private Long id;
//
//
//    @ManyToOne(fetch = FetchType.LAZY)      //plan 과 매핑
//    @JoinColumn(name = "plan_id")
//    private InvitePlan plan;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "location_id")
//    private Location location;
//}
