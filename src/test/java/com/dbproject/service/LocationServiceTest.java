package com.dbproject.service;

import com.dbproject.entity.Location;
import com.dbproject.repository.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class LocationServiceTest {

    @Autowired
    private LocationRepository locationRepository;


    @DisplayName("추천 장소의 Id 를 주면 그 추천 장소를 가져온다")
    @Test
    void getLocationDtl(){
        //given
        String locationId = "C1_379000000A_001572";

        //when
        Location location = locationRepository.findByLocationId(locationId);

        //then
        assertThat(location.getName()).isEqualTo("西門町");
        assertThat(location.getRegion()).isEqualTo("臺北市");

     }

     @DisplayName("추천 도시 이름과 pageable 로 추천 도시의 추천 장소 리스트를 페이징하여 돌려준다")
     @Test
     void getLocationPageByCity(){

         //given
         //0번째 페이지
         Pageable pageable = PageRequest.of(0, 5);
         String city = "臺北市";

         //when
         Page<Location> locationListPage = locationRepository.getLocationPageByCity(city, pageable);
         List<Location> pageContent = locationListPage.getContent();

         //then
         assertThat(locationListPage.getTotalPages()).isEqualTo(90);
         assertThat(locationListPage.getTotalElements()).isEqualTo(449);
         assertThat(pageContent).hasSize(5);

     }
}