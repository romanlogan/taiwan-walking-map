package com.dbproject.service;

import com.dbproject.entity.City;
import com.dbproject.entity.Location;
import com.dbproject.entity.LocationPicture;
import com.dbproject.entity.Route;
import com.dbproject.repository.CityImgRepository;
import com.dbproject.repository.CityRepository;
import com.dbproject.repository.LocationRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class ExploreServiceTest {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private CityImgRepository cityImgRepository;

    @Autowired
    private CityRepository cityRepository;

    @Autowired
    private RouteService routeService;


    @Test
    @DisplayName("도시 찾기 테스트")
    public void getCityByCityNameTest() {

        String[] cityArr = {"臺北市", "新北市", "臺中市", "臺南市", "臺東縣", "高雄市", "花蓮縣", "桃園市"};
        for (int i = 0; i < cityArr.length; i++) {

            City city = cityRepository.findBypostalAddressCity(cityArr[i]);
            assertThat(city.getPostalAddressCity(), is(cityArr[i]));

        }
    }

    @Test
    @DisplayName("타이베이 도시 페이지에서 이미지가 있는 추천 장소(4곳) 찾기 테스트")
    public void get4RecommendLocationThatHasImgInCityPageTest2() {

        String[] cityArr = {"臺北市"};

        for (int i = 0; i < cityArr.length; i++) {
            //클래스 안에서 메서드로 분리한 코드를 테스트 할때는 메서드를 호출하여 테스트 해야하나 아니면 메서드 안의 내용을 테스트 해야 하나?

            List<Location> LocationList = locationRepository.findByRegion(cityArr[i]);

            List<Location> hasImgLocationList = new ArrayList<>();

            for (int j = 0; j < LocationList.size(); j++) {

                Location location = LocationList.get(i);
                System.out.println("location = " + location);
                LocationPicture locationPicture = location.getLocationPictureMethod();
                System.out.println("locationPicture = " + locationPicture);
                String pictureUrl = locationPicture.getPicture1();

                if (pictureUrl == null) {
                    continue;
                }

                hasImgLocationList.add(LocationList.get(j));

                if (hasImgLocationList.size() == 4) {
                    break;
                }

            }

            assertThat(hasImgLocationList.size(), is(4));
        }
    }

    @Test
    @DisplayName("도시 페이지에서 이미지가 있는 추천 장소(4곳) 찾기 테스트")
    public void get4RecommendLocationThatHasImgInCityPageTest() {

        String[] cityArr = {"臺北市", "新北市", "臺中市", "臺南市", "臺東縣", "高雄市", "花蓮縣", "桃園市"};

        for (int i = 0; i < cityArr.length; i++) {
            //클래스 안에서 메서드로 분리한 코드를 테스트 할때는 메서드를 호출하여 테스트 해야하나 아니면 메서드 안의 내용을 테스트 해야 하나?

            List<Location> LocationList = locationRepository.findByRegion(cityArr[i]);

            List<Location> hasImgLocationList = new ArrayList<>();

            for (int j = 0; j < LocationList.size(); j++) {

                Location location = LocationList.get(i);
                System.out.println("location = " + location);
                LocationPicture locationPicture = location.getLocationPictureMethod();
                System.out.println("locationPicture = " + locationPicture);
                String pictureUrl = locationPicture.getPicture1();

                if (pictureUrl == null) {
                    continue;
                }

                hasImgLocationList.add(LocationList.get(j));

                if (hasImgLocationList.size() == 4) {
                    break;
                }

            }

            assertThat(hasImgLocationList.size(), is(4));
        }
    }

    @Test
    @DisplayName("추천 루트 가져오기 테스트")
    public void getRecommendRouteTest() {

        String[] cityArr = {"臺北市", "新北市", "臺中市", "臺南市", "臺東縣", "高雄市", "花蓮縣", "桃園市"};

        for (int i = 0; i < cityArr.length-1; i++) {

            List<Route> routeList = routeService.getRouteList(cityArr[i]);

            assertThat(routeList.size(), is(2));
        }
    }

    @Test
    @DisplayName("추천 루트 가져오기 실패 테스트")
    public void getEmptyRecommendRouteTest() {

        String city = "桃園市";
        List<Route> routeList = routeService.getRouteList(city);
        assertThat(routeList.size(), is(0));
    }

    private long run(int times, Runnable func) {
        long start = System.nanoTime();
        for (int i = 0; i < times; i++) {
            func.run();

        }

        long stop = System.nanoTime();
        return (stop - start) / 1000000;
    }

//    @Test
//    @DisplayName("추천 루트 가져오기 성능 테스트")
//    public void getRecommendRoutePerformanceTest() {
//
//        String city = "桃園市";
//        int numberOfTimes = 1000;
//
//        long elapseMs = run(numberOfTimes, (Runnable) routeService.getRouteList(city));
//
//        assertTrue(elapseMs < 1000);
//    }






}