package com.dbproject.api.explore;

import com.dbproject.api.city.City;
import com.dbproject.api.city.CityImg;
import com.dbproject.api.location.Location;
import com.dbproject.api.route.Route;
import com.dbproject.api.route.RouteDto;
import com.dbproject.api.route.RouteService;
import com.dbproject.api.route.RouteLocationDto;
import com.dbproject.api.city.CityImgRepository;
import com.dbproject.api.city.CityRepository;
import com.dbproject.api.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExploreService {

    private final LocationRepository locationRepository;

    private final CityImgRepository cityImgRepository;

    private final CityRepository cityRepository;

    private final RouteService routeService;


//    public RouteDto setRoutePoint(RouteDto routeDto, String point) {
//
//        String pointImgUrl = findRouteLocationUrl(point);
//        String pointDesc = findRouteLocationDescribe(point);
//
//        //여기서 막힘
//        routeDto.
//
//
//
////        String startPointImgUrl = findRouteLocationUrl(startPoint);
////        String startPointDesc = findRouteLocationDescribe(startPoint);
////        routeDto.setStartPointName(startPoint);
////        routeDto.setStartPointImgUrl(startPointImgUrl);
////        routeDto.setStartPointDescribe(startPointDesc);
//
//    }



    public CityDto getLocationDtl(String postalAddressCity) {

        List<Route> routeList = routeService.getRouteList(postalAddressCity);
        City city = cityRepository.findBypostalAddressCity(postalAddressCity);

//        BigDecimal lat = city.getPositionLat();
//        BigDecimal lon = city.getPositionLon();
//        System.out.println("lat :" + lat + " ,lng :  " + lon);

        CityDto cityDto = getLocationListByCity(postalAddressCity);
        cityDto.setPositionLat(city.getPositionLat());
        cityDto.setPositionLon(city.getPositionLon());

        List<RouteDto> routeDtoList = cityDto.getRouteDtoList();

        for (Route route : routeList) {
//            int num = route.getId();
            RouteDto routeDto = new RouteDto();
            routeDto.setRouteNum(route.getId());

//            List<RouteLocationDto> routeLocationDtoList = routeDto.getRouteLocationDtoList();

            String startPoint = route.getStartPoint();

            String startPointImgUrl = findRouteLocationUrl(startPoint);
            String startPointDesc = findRouteLocationDescribe(startPoint);
            routeDto.setStartPointName(startPoint);
            routeDto.setStartPointImgUrl(startPointImgUrl);
            routeDto.setStartPointDescribe(startPointDesc);


            String wayPoint1 = route.getWayPoint1();

            String wayPoint1ImgUrl = findRouteLocationUrl(wayPoint1);
            String wayPoint1Desc = findRouteLocationDescribe(wayPoint1);
            routeDto.setWayPoint1Name(wayPoint1);
            routeDto.setWayPoint1ImgUrl(wayPoint1ImgUrl);
            routeDto.setWayPoint1Describe(wayPoint1Desc);

            String wayPoint2 = route.getWayPoint2();

            String wayPoint2ImgUrl = findRouteLocationUrl(wayPoint2);
            String wayPoint2Desc = findRouteLocationDescribe(wayPoint2);
            routeDto.setWayPoint2Name(wayPoint2);
            routeDto.setWayPoint2ImgUrl(wayPoint2ImgUrl);
            routeDto.setWayPoint2Describe(wayPoint2Desc);

            String arrivePoint = route.getArrivePoint();

            String arrivePointImgUrl = findRouteLocationUrl(arrivePoint);
            String arrivePointDesc = findRouteLocationDescribe(arrivePoint);
            routeDto.setArrivePointName(arrivePoint);
            routeDto.setArrivePointImgUrl(arrivePointImgUrl);
            routeDto.setArrivePointDescribe(arrivePointDesc);

//            img 값이 null 이면 ?

            routeDtoList.add(routeDto);
        }
        return cityDto;
    }

    public RouteLocationDto findRouteLocation(String routeLocationNm) {

        Location routeLocation = locationRepository.findByName(routeLocationNm);
        RouteLocationDto routeLocationDto = RouteLocationDto.of(routeLocation);

        return routeLocationDto;
    }

    public String findRouteLocationUrl(String routeLocationName) {

        Location routeLocation = locationRepository.findByName(routeLocationName);
        return routeLocation.getLocationPicture().getPicture1();
    }

    public String findRouteLocationDescribe(String routeLocationName) {

        Location routeLocation = locationRepository.findByName(routeLocationName);
        return routeLocation.getDescription();
    }

    public CityDto getLocationListByCity(String cityName) {

        List<Location> tempLocationList = locationRepository.findByRegion(cityName);

        List<Location> locationList = new ArrayList<>();

        for (int i = 0; i < tempLocationList.size(); i++) {

            if (tempLocationList.get(i).getLocationPicture().getPicture1() == null) {
                continue;
            }

            if (locationList.size() == 4) {
                break;
            }

            locationList.add(tempLocationList.get(i));
        }


        List<CityImg> cityImgList = cityImgRepository.findByPostalAddressCity(cityName);

        City city = cityRepository.findBypostalAddressCity(cityName);

        CityDto cityDto = new CityDto(city.getPostalAddressCity(), city.getCityDetail(), locationList, cityImgList);

        return cityDto;
    }


}
