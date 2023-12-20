package com.dbproject.repository;

import com.dbproject.dto.FastSearchDto;
import com.dbproject.entity.Location;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LocationRepositoryTest {


    @Autowired
    private LocationRepository locationRepository;

    @DisplayName("도시 이름으로 그 지역의 추천 장소를 가져온다")
    @Test
    void findByRegion(){

        //given
        String cityName = "臺北市";

        //when
        List<Location> locationList = locationRepository.findByRegion(cityName);

        //then
        assertThat(locationList).hasSize(449)
                .extracting("region")
                .contains("臺北市");

        for (int i = 0; i < locationList.size(); i++) {
            Location location = locationList.get(i);
            assertThat(location.getRegion()).isEqualTo("臺北市");
        }
     }

     @DisplayName("장소 이름을 주면 그 장소를 찾아준다")
     @Test
     void findByAttractionName(){
         //given
         String name = "西門町";

         //when
         Location location = locationRepository.findByName(name);

         //then
         assertThat(location.getName()).isEqualTo("西門町");

      }

    @DisplayName("장소 아이디을 주면 그 장소를 찾아준다")
    @Test
    void findByLocationId(){
        //given
        String locationId = "C1_379000000A_001572";

        //when
        Location location = locationRepository.findByLocationId(locationId);

        //then
        assertThat(location.getName()).isEqualTo("西門町");

    }


    @DisplayName("검색하고 싶은 이름과 도시를 받으면 추천 장소를 페이징 하여 받는다")
    @Test
    void getLocationPageBySearch(){
        //given
        FastSearchDto fastSearchDto = FastSearchDto.create("大學", "臺北市");
        Pageable pageable = PageRequest.of(0, 5);

        //when
        Page<Location> locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);

        //then
        assertThat(locationPage.getTotalElements()).isEqualTo(7);
        assertThat(locationPage.getTotalPages()).isEqualTo(2);
    }




}