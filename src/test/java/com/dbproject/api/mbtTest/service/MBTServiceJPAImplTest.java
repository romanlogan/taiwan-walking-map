package com.dbproject.api.mbtTest.service;

import com.dbproject.api.city.City;
import com.dbproject.api.mbtTest.MBTCityDtoCamel;
import com.dbproject.api.mbtTest.repository.MBTRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MBTServiceJPAImplTest {

    @Autowired
    private MBTRepository mbtRepository;

    @DisplayName("")
    @Test
    void test(){
        //given
        String cityName = "臺北市";
        City city = mbtRepository.findByRegion(cityName);
        MBTCityDtoCamel cityDto = new MBTCityDtoCamel(city.getPostalAddressCity(), city.getCityDetail(), city.getPositionLat(), city.getPositionLon());
        cityDto.printDetail();
     }

}