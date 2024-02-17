package com.dbproject.api.mbtTest.service;

import com.dbproject.api.city.City;
import com.dbproject.api.mbtTest.MBTCityDto;
import com.dbproject.api.mbtTest.repository.MBTMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Qualifier("MBTServiceImpl")
public class MBTServiceImpl implements MBTService {

    @Autowired
    MBTMapper mapper;

    @Override
    public MBTCityDto getCity() {

        String cityName = "臺北市";
        String str = mapper.selectCity(cityName);

        System.out.println("mybatis : "+str);
//        MBTCityDto mbtCityDto = new MBTCityDto(city.getPostalAddressCity(), city.getCityDetail());
        MBTCityDto mbtCityDto = new MBTCityDto();
        return mbtCityDto;
    }
}
