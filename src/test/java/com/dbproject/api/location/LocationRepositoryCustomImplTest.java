package com.dbproject.api.location;

import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
import com.dbproject.api.quickSearch.dto.QuickSearchLocationDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
}