package com.dbproject.service;

import com.dbproject.api.quickSearch.QuickSearchService;
import com.dbproject.api.quickSearch.dto.FastSearchDto;
import com.dbproject.api.quickSearch.dto.QuickSearchListResponse;
import com.dbproject.api.quickSearch.dto.QuickSearchPageResponse;
import com.dbproject.api.quickSearch.dto.QuickSearchResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class QuickSearchServiceTest {

    @Autowired
    private QuickSearchService quickSearchService;


    @DisplayName("검색하고 싶은 이름과 도시로 추천 장소 페이지 와 city 객체를 받는다")
    @Test
    void getLocationPageAndCityBySearchQuery(){

        //given
        FastSearchDto fastSearchDto = FastSearchDto.create("大學", "臺北市");
        Pageable pageable = PageRequest.of(0, 5);

        //when
        QuickSearchPageResponse quickSearchResultDto = quickSearchService.getQuickSearchPage(fastSearchDto, pageable);

        //then
        assertThat(quickSearchResultDto.getLocationPage().getTotalElements()).isEqualTo(7);
        assertThat(quickSearchResultDto.getLocationPage().getTotalPages()).isEqualTo(2);
        assertThat(quickSearchResultDto.getCity().getPostalAddressCity()).isEqualTo("臺北市");
    }


    @DisplayName("검색하고 싶은 검색어와 도시로 추천 장소 를 10개 리스트로 찾는다")
    @Test
    void getQuickSearchList() {

        //given
        FastSearchDto fastSearchDto = FastSearchDto.create("公園", "臺北市");
        Pageable pageable = PageRequest.of(0, 10);

        //when
        QuickSearchListResponse quickSearchListResponse = quickSearchService.getQuickSearchList(fastSearchDto, pageable);

        //then
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList()).hasSize(10);
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList().get(0).getName()).isEqualTo("140高地公園");
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList().get(0).getRegion()).isEqualTo("臺北市");
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList().get(0).getLocationId()).isEqualTo("C1_379000000A_000406");
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList().get(9).getName()).isEqualTo("北投社三層崎公園");
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList().get(9).getRegion()).isEqualTo("臺北市");
        assertThat(quickSearchListResponse.getQuickSearchLocationDtoList().get(9).getLocationId()).isEqualTo("C1_379000000A_002197");
        assertThat(quickSearchListResponse.getCityNameList()).hasSize(8);
        assertThat(quickSearchListResponse.getCity().getPostalAddressCity()).isEqualTo("臺北市");

    }
}