package com.dbproject.api.route;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.constant.RouteStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RouteDto {

    private Long id;
    private Integer day;
    private RouteStatus routeStatus;

    private List<LocationDto> locationList = new ArrayList<>();

    @Builder
    public RouteDto(Long id, Integer day, RouteStatus routeStatus) {
        this.id = id;
        this.day = day;
        this.routeStatus = routeStatus;
    }

    public static RouteDto createRouteDto(Route route) {

        return RouteDto.builder()
                .id(route.getId())
                .day(route.getDay())
                .routeStatus(route.getRouteStatus())
                .build();
    }
}
