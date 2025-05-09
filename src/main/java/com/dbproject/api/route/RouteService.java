package com.dbproject.api.route;

import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final LocationRepository locationRepository;

//    public List<Route> getRouteList(String postalAddressCity) {
//
//        List<Route> routeList = routeRepository.findByPostalAddressCity(postalAddressCity);
//
//        return routeList;
//    }

//    public RouteDtlDto getRouteDtl(Integer routeNum) {
//
//        Route route = routeRepository.findRouteById(routeNum);
//
//        String startPointName = route.getStartPoint();
//        Location startPoint = locationRepository.findByName(startPointName);
//        StartPointDto startPointDto = StartPointDto.of(startPoint);
//
//        System.out.println(startPointDto.toString());
//
//        String wayPoint1Name = route.getWayPoint1();
//        Location wayPoint1 = locationRepository.findByName(wayPoint1Name);
//        WayPoint1Dto wayPoint1Dto = WayPoint1Dto.of(wayPoint1);
//
//        String wayPoint2Name = route.getWayPoint2();
//        Location wayPoint2 = locationRepository.findByName(wayPoint2Name);
//        WayPoint2Dto wayPoint2Dto = WayPoint2Dto.of(wayPoint2);
//
//        String arrivePointName = route.getArrivePoint();
//        Location arrivePoint = locationRepository.findByName(arrivePointName);
//        ArrivePointDto arrivePointDto = ArrivePointDto.of(arrivePoint);
//
//        RouteDtlDto routeDtlDto = new RouteDtlDto();
//        routeDtlDto.setStartPointDto(startPointDto);
//        routeDtlDto.setWayPoint1Dto(wayPoint1Dto);
//        routeDtlDto.setWayPoint2Dto(wayPoint2Dto);
//        routeDtlDto.setArrivePointDto(arrivePointDto);
//
//        return routeDtlDto;
//    }
}
