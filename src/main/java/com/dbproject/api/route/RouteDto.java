package com.dbproject.api.route;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.routeLocation.RouteLocation;
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

    private List<LocationDto> locationDtos = new ArrayList<>();

    @Builder
    public RouteDto(Long id, Integer day, RouteStatus routeStatus, List<LocationDto> locationList) {
        this.id = id;
        this.day = day;
        this.routeStatus = routeStatus;
        this.locationDtos = locationList;
    }

    public static RouteDto createRouteDto(Route route) {

        List<LocationDto> locationDtos = new ArrayList<>();

        List<RouteLocation> routeLocations = route.getRouteLocationList();
        for (RouteLocation routeLocation : routeLocations) {
            Location location = routeLocation.getLocation();
            LocationDto locationDto = LocationDto.from(location);

            locationDtos.add(locationDto);
        }


        return RouteDto.builder()
                .id(route.getId())
                .day(route.getDay())
                .routeStatus(route.getRouteStatus())
                .locationList(locationDtos)
                .build();
    }

    public static List<RouteDto> createRouteDtosFrom(List<Route> routeList) {

        List<RouteDto> routeDtos = new ArrayList<>();

        for (Route route : routeList) {
            routeDtos.add(createRouteDto(route));
        }

        return routeDtos;

    }
}
