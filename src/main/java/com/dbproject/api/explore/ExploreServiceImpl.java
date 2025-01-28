package com.dbproject.api.explore;

import com.dbproject.api.city.City;
import com.dbproject.api.city.dto.CityDto;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.route.RouteService;
import com.dbproject.api.route.RouteLocationDto;
import com.dbproject.api.city.CityImgRepository;
import com.dbproject.api.city.CityRepository;
import com.dbproject.api.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExploreServiceImpl implements ExploreService{

    private final LocationRepository locationRepository;

    private final CityImgRepository cityImgRepository;

    private final CityRepository cityRepository;

    private final RouteService routeService;


    public CityDto getLocationDtl(String cityName) {

        CityDto cityDto = new CityDto();

        setCityInfo(cityName, cityDto);
        setRecommendLocationList(cityName, cityDto);

        // route 추가
        // 여기 하기전에 먼저 invitePlan 을 Plan 으로 바꾸는 부분부터 수정하기

        return cityDto;
    }

    private void setCityInfo(String cityName, CityDto cityDto) {

        City city = cityRepository.findByRegion(cityName);
        cityDto.setCityInfo(city);

    }

    private void setRecommendLocationList(String cityName, CityDto cityDto) {

        List<LocationDto> locationDtoList = locationRepository.findTop10RecommendLocationList(cityName);
        cityDto.setLocationDtoList(locationDtoList);

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
}
