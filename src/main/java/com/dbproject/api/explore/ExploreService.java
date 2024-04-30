package com.dbproject.api.explore;

import com.dbproject.api.city.City;
import com.dbproject.api.city.CityImg;
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

    public CityDto getLocationDtl(String cityName) {

        CityDto cityDto = new CityDto();
        setCityBasicInfo(cityName, cityDto);
        setCityImgList(cityName, cityDto);
        setRecommendLocationList(cityName, cityDto);       // 즐겨찾기가 많은 4곳으로 변경하기

        // route 추가
        //여기 하기전에 먼저 invitePlan 을 Plan 으로 바꾸는 부분부터 수정하기

        return cityDto;
    }

    private void setCityBasicInfo(String cityName, CityDto cityDto) {

        City city = cityRepository.findBypostalAddressCity(cityName);
        cityDto.setCityInfo(city);
    }

    private void setCityImgList(String cityName, CityDto cityDto) {

        List<CityImg> cityImgList = cityImgRepository.findByPostalAddressCity(cityName);
        cityDto.setCityImgList(cityImgList);

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

    public void setRecommendLocationList(String cityName, CityDto cityDto) {

//        사진이 있는 장소중 즐겨찾기 개수가 가장 많은곳 ?
//      즐겨찾기 개수가 가장 많은곳 4곳 (사진 미포함 ? )

        List<LocationDto> locationDtoList = locationRepository.findTop10RecommendLocationList(cityName);
        cityDto.setLocationDtoList(locationDtoList);

////        List<Location> tempLocationList = locationRepository.findByRegion(cityName);
////        List<Location> locationList = new ArrayList<>();
////
////        for (int i = 0; i < tempLocationList.size(); i++) {
////
////            if (tempLocationList.get(i).getLocationPicture().getPicture1() == null) {
////                continue;
////            }
////
////            if (locationList.size() == 4) {
////                break;
////            }
////
////            locationList.add(tempLocationList.get(i));
////        }
//
//        cityDto.setLocationList(locationList);
    }


}
