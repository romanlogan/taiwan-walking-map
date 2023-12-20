package com.dbproject.service;

import com.dbproject.dto.FastSearchDto;
import com.dbproject.dto.QuickSearchResultDto;
import com.dbproject.entity.Location;
import com.dbproject.repository.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
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
        QuickSearchResultDto quickSearchResultDto = quickSearchService.getQuickSearchPage(fastSearchDto, pageable);


        //then
        assertThat(quickSearchResultDto.getLocationPage().getTotalElements()).isEqualTo(7);
        assertThat(quickSearchResultDto.getLocationPage().getTotalPages()).isEqualTo(2);
        assertThat(quickSearchResultDto.getCity().getPostalAddressCity()).isEqualTo("臺北市");
    }


}