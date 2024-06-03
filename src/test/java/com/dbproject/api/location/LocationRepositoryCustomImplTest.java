package com.dbproject.api.location;

import com.dbproject.api.location.dto.RecommendLocationDto;
import com.dbproject.api.location.dto.RecommendLocationListRequest;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
import com.dbproject.api.quickSearch.dto.QuickSearchLocationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class LocationRepositoryCustomImplTest {

    @Autowired
    private LocationRepository locationRepository;

    @DisplayName("")
    @Test
    void getLocationListByCond(){
        //given
        String searchQuery = "公園";
        String searchCity = "臺北市";
        String searchTown = "大安區";
        String orderType = "name";
        String openTimeCond = "true";
        String feeCond = "";
        String picCond = "";
        QuickSearchFormRequest quickSearchFormRequest = new QuickSearchFormRequest(searchQuery, searchCity, searchTown, orderType, openTimeCond, feeCond, picCond);
        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<QuickSearchLocationDto> locationDtoList = locationRepository.getLocationListByCond(quickSearchFormRequest, pageable);

        //then
        assertThat(locationDtoList).hasSize(3);
        assertThat(locationDtoList.get(0).getName()).isEqualTo("大安森林公園");
        assertThat(locationDtoList.get(1).getName()).isEqualTo("富陽自然生態公園");
        assertThat(locationDtoList.get(2).getName()).isEqualTo("敦安公園");
     }

    @DisplayName("도시의 추천 장소 리스트를 페이징하여 돌려준다")
    @Test
    void getLocationPageByCity(){
        //given
        //0번째 페이지
        Pageable pageable = PageRequest.of(0, 5);
        RecommendLocationListRequest request =
                new RecommendLocationListRequest("臺北市", "公園", "中山區");

        //when
        Page<RecommendLocationDto> locationListPage = locationRepository.getLocationPageByCity(request, pageable);

        //then
        assertThat(locationListPage.getTotalPages()).isEqualTo(3);
        assertThat(locationListPage.getTotalElements()).isEqualTo(12);
        assertThat(locationListPage.getContent()).hasSize(5);
    }


    @DisplayName("도시의 추천 장소 리스트를 페이징하여 돌려줄때, " +
            "searchQuery 와 searchTown 이 empty string 이면 그 도시의 모든 장소를 가져온다 ")
    @Test
    void getLocationPageByCityWithEmptySearchQueryAndSearchTown(){
        //given
        //0번째 페이지
        Pageable pageable = PageRequest.of(0, 5);
        RecommendLocationListRequest request =
                new RecommendLocationListRequest("臺北市", "", "");

        //when
        Page<RecommendLocationDto> locationListPage = locationRepository.getLocationPageByCity(request, pageable);

        //then
        assertThat(locationListPage.getTotalPages()).isEqualTo(90);
        assertThat(locationListPage.getTotalElements()).isEqualTo(449);
        assertThat(locationListPage.getContent()).hasSize(5);
    }

    @DisplayName("도시의 추천 장소 리스트를 페이징하여 돌려줄때, " +
            "searchQuery 가 empty string 이면 그 도시의 town 의 모든 장소를 가져온다 ")
    @Test
    void getLocationPageByCityWithEmptySearchQuery(){
        //given
        //0번째 페이지
        Pageable pageable = PageRequest.of(0, 5);
        RecommendLocationListRequest request =
                new RecommendLocationListRequest("臺北市", "", "中山區");

        //when
        Page<RecommendLocationDto> locationListPage = locationRepository.getLocationPageByCity(request, pageable);

        //then
        assertThat(locationListPage.getTotalPages()).isEqualTo(12);
        assertThat(locationListPage.getTotalElements()).isEqualTo(57);
        assertThat(locationListPage.getContent()).hasSize(5);
    }


}