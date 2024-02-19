package com.dbproject.api.mbtTest.service;

import com.dbproject.api.city.City;
import com.dbproject.api.mbtTest.MBTCityDto;
import com.dbproject.api.mbtTest.repository.MBTRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//@Service
//@Transactional
//@RequiredArgsConstructor
//@Qualifier("MBTServiceJPAImpl")
//public class MBTServiceJPAImpl implements MBTService{
//
//    private final MBTRepository mbtRepository;
//
//    @Override
//    public MBTCityDto getCity() {
//
//        String cityName = "臺北市";
//        City city = mbtRepository.findBypostalAddressCity(cityName);
//
//        System.out.println("jpa : "+city.getCityDetail());
//        MBTCityDto cityDto = new MBTCityDto(city.getPostalAddressCity(), city.getCityDetail());
//        return cityDto;
//    }
//
//}
