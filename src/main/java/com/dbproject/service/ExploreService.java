package com.dbproject.service;

import com.dbproject.dto.*;
import com.dbproject.entity.City;
import com.dbproject.entity.CityImg;
import com.dbproject.entity.Location;
import com.dbproject.entity.Route;
import com.dbproject.repository.CityImgRepository;
import com.dbproject.repository.CityRepository;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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



    public Page<Location> getLocationPageByCity(SearchByCityDto searchByCityDto, Pageable pageable) {

        Page<Location> locationList = locationRepository.getLocationPageByCity(searchByCityDto.getArriveCity(), pageable);

        return locationList;
    }

    public RouteLocationDto findRouteLocation(String routeLocationNm) {

        Location routeLocation = locationRepository.findByAttractionName(routeLocationNm);
        RouteLocationDto routeLocationDto = RouteLocationDto.of(routeLocation);

        return routeLocationDto;
    }

    public String findRouteLocationUrl(String routeLocationName) {

        Location routeLocation = locationRepository.findByAttractionName(routeLocationName);
        return routeLocation.getImagesUrl();
    }

    public String findRouteLocationDescribe(String routeLocationName) {

        Location routeLocation = locationRepository.findByAttractionName(routeLocationName);
        return routeLocation.getDescription();
    }

    public CityDto getLocationDtl(String postalAddressCity) {

        List<Route> routeList = routeService.getRouteList(postalAddressCity);

        City city = cityRepository.findBypostalAddressCity(postalAddressCity);
        BigDecimal lat = city.getPositionLat();
        BigDecimal lon = city.getPositionLon();
        System.out.println("lat :" + lat + " ,lng :  " + lon);
        CityDto cityDto = getLocationListByCity(postalAddressCity);
        cityDto.setPositionLat(lat);
        cityDto.setPositionLon(lon);

        List<RouteDto> routeDtoList = cityDto.getRouteDtoList();

        for (Route route : routeList) {
            int num = route.getId();

            RouteDto routeDto = new RouteDto();
            routeDto.setRouteNum(num);

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

    public CityDto getLocationListByCity(String cityName) {

        List<Location> tempLocationList = locationRepository.findByPostalAddressCity(cityName);

        List<Location> locationList = new ArrayList<>();

        for (int i = 0; i < tempLocationList.size(); i++) {

            if (tempLocationList.get(i).getImagesUrl() == null) {
                continue;
            }

            if (locationList.size() == 4) {
                break;
            }

            locationList.add(tempLocationList.get(i));
        }

        List<CityImg> cityImgList = cityImgRepository.findByPostalAddressCity(cityName);

        City city = cityRepository.findBypostalAddressCity(cityName);

        System.out.println("********************************************");
        System.out.println(city.getPostalAddressCity());
        System.out.println("********************************************");

        CityDto cityDto = new CityDto(city.getPostalAddressCity(), city.getCityDetail(), locationList, cityImgList);



        return cityDto;
    }

    public Page<Location> getLocationListBySearchQuery(FastSearchDto fastSearchDto, Pageable pageable) {

        Page<Location> locationPage;

        if (fastSearchDto.getSearchCity() == null) {
            locationPage = locationRepository.getLocationPageByBtn(fastSearchDto, pageable);
        }else {
            locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);
        }

        return locationPage;

    }

}
