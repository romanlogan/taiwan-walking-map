package com.dbproject.api.quickSearch;


import com.dbproject.api.city.City;
import com.dbproject.api.city.CityRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.quickSearch.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class QuickSearchService {

    private final LocationRepository locationRepository;

    private final CityRepository cityRepository;

    public QuickSearchPageResponse getQuickSearchPage(FastSearchDto fastSearchDto, Pageable pageable) {

        QuickSearchPageResponse quickSearchPageResponse = new QuickSearchPageResponse();

        //추천 장소 페이징 저장
        findLocationPage(quickSearchPageResponse, fastSearchDto ,pageable);

        //도시 정보 가져오기()
        findCityDtl(quickSearchPageResponse, fastSearchDto.getSearchCity());

        return quickSearchPageResponse;
    }

    //    이거는 리스트 페이지 처음 들어왔을때 (정렬이나 다른 조건 없음 )
    public QuickSearchListResponse getQuickSearchList(FastSearchDto fastSearchDto, Pageable pageable) {

        QuickSearchListResponse quickSearchListResponse = new QuickSearchListResponse();
        //장소 리스트 찾기
        findLocationList(quickSearchListResponse, fastSearchDto, pageable);

        //선택된 도시 의 정보를 가져온다
        findCityDtl(quickSearchListResponse, fastSearchDto.getSearchCity());
        findCityList(quickSearchListResponse);

        return quickSearchListResponse;

    }

    private void findCityList(QuickSearchResponse quickSearchResponse) {
        List<City> cityList = cityRepository.findAll();
        List<String> cityNameList = new ArrayList<>();

        for (City city : cityList) {
            cityNameList.add(city.getPostalAddressCity());
        }

        quickSearchResponse.setCityNameList(cityNameList);
    }


    // 상속과 다형성을 이용한 QuickSearchResponse 자식들의 메소드 같이 사용
    private void findCityDtl(QuickSearchResponse quickSearchResponse, String searchCity) {

        City city = cityRepository.findBypostalAddressCity(searchCity);
        QuickSearchCityDto quickSearchCityDto = QuickSearchCityDto.of(city);

        quickSearchResponse.setCity(quickSearchCityDto);
    }


    public void findLocationPage(QuickSearchPageResponse quickSearchPageResponse,
                                 FastSearchDto fastSearchDto,
                                 Pageable pageable) {

        Page<QuickSearchLocationDto> locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);
        quickSearchPageResponse.setLocationPage(locationPage);
    }


    public void findLocationList(QuickSearchListResponse quickSearchListResponse,
                                 FastSearchDto fastSearchDto,
                                 Pageable pageable) {


        List<Location> locationList = locationRepository.findBySearchQueryAndSearchCity(fastSearchDto.getSearchQuery(), fastSearchDto.getSearchCity(), pageable);
        List<QuickSearchLocationDto> locationDtoList = new ArrayList<>();

        for (Location location : locationList) {
            QuickSearchLocationDto quickSearchLocationDto = QuickSearchLocationDto.from(location);
            locationDtoList.add(quickSearchLocationDto);
        }
        quickSearchListResponse.setQuickSearchLocationDtoList(locationDtoList);

    }



    //파라미터가 QuickSearchResultDto 로 되어있어서 재사용이 불가
//    private void findCityDtl(QuickSearchResultDto quickSearchResultDto, String searchCity) {
//
//        City city = cityRepository.findBypostalAddressCity(searchCity);
//        QuickSearchCityDto quickSearchCityDto = QuickSearchCityDto.of(city);
//
//        quickSearchResultDto.setCity(quickSearchCityDto);
//    }
//
//
//    public void findLocationPage(QuickSearchResultDto quickSearchResultDto,
//                                                 FastSearchDto fastSearchDto,
//                                                 Pageable pageable) {
//
//        Page<QuickSearchLocationDto> locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);
//        quickSearchResultDto.setLocationPage(locationPage);
//    }



}
