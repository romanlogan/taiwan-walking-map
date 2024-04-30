package com.dbproject.api.routeLocation;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.plan.Plan;
import com.dbproject.api.route.Route;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class RouteLocation extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_location_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)      //plan 과 매핑
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    public RouteLocation() {
    }

    @Builder
    public RouteLocation(Route route, Location location) {
        this.route = route;
        this.location = location;
    }

    public static RouteLocation createRouteLocation(Route route, Location location) {

        return RouteLocation.builder()
                .route(route)
                .location(location)
                .build();
    }
}
