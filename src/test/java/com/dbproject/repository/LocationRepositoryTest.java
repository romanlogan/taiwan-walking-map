package com.dbproject.repository;

import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.quickSearch.dto.FastSearchDto;
import com.dbproject.api.quickSearch.dto.QuickSearchLocationDto;
import com.dbproject.api.location.dto.RecLocationListRequest;
import com.dbproject.api.location.dto.RecLocationListResponse;
import com.dbproject.api.location.Location;
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

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
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
        Page<QuickSearchLocationDto> locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);

        //then
        assertThat(locationPage.getTotalElements()).isEqualTo(7);
        assertThat(locationPage.getTotalPages()).isEqualTo(2);
    }

    @DisplayName("장소 아이디을 주면 그 장소의 이미지를 가져온다")
    @Test
    void test(){
        //given
        String locationId = "C1_379000000A_001572";

        //when
        Location location = locationRepository.findByLocationId(locationId);

        //then
        assertThat(location.getLocationPicture().getPicture1()).isNotNull();
    }

    @DisplayName("추천 도시 이름과 pageable 로 추천 도시의 추천 장소 리스트를 페이징하여 돌려준다")
    @Test
    void getLocationPageByCity(){

        //given
        //0번째 페이지
        Pageable pageable = PageRequest.of(0, 5);
        String city = "臺北市";
        RecLocationListRequest request = new RecLocationListRequest(city);

        //when
        Page<RecLocationListResponse> locationListPage = locationRepository.getLocationPageByCity(request, pageable);
        List<RecLocationListResponse> pageContent = locationListPage.getContent();

        //then
        assertThat(locationListPage.getTotalPages()).isEqualTo(90);
        assertThat(locationListPage.getTotalElements()).isEqualTo(449);
        assertThat(pageContent).hasSize(5);
    }

    @DisplayName("검색어와 도시 이름으로 장소 리스트를 가져옵니다 (LIMIT 10)")
    @Test
    void findBySearchQueryAndSearchCity(){
        //given
        String searchCity = "臺北市";
        String searchQuery = "公園";
        Pageable pageable = PageRequest.of(0, 10);

        //when
        List<Location> locationList = locationRepository.findBySearchQueryAndSearchCity(searchQuery, searchCity, pageable);

        //then
        assertThat(locationList).hasSize(10);
        assertThat(locationList.get(0).getName()).isEqualTo("140高地公園");
        assertThat(locationList.get(0).getRegion()).isEqualTo("臺北市");
        assertThat(locationList.get(0).getLocationId()).isEqualTo("C1_379000000A_000406");

        assertThat(locationList.get(9).getName()).isEqualTo("北投社三層崎公園");
        assertThat(locationList.get(9).getRegion()).isEqualTo("臺北市");
        assertThat(locationList.get(9).getLocationId()).isEqualTo("C1_379000000A_002197");
     }

     @DisplayName("도시 이름으로 중복 제거된 Town 이름 리스트를 가져옵니다")
     @Test
     void findTownListByRegion(){

         //given
         String searchCity = "臺北市";

         //when
         List<String> townList = locationRepository.findTownListByRegion(searchCity);

         //then
         assertThat(townList).hasSize(12);
         assertThat(townList.get(0)).isEqualTo("中山區");
         assertThat(townList.get(1)).isEqualTo("中正區");
         assertThat(townList.get(2)).isEqualTo("信義區");


      }

}