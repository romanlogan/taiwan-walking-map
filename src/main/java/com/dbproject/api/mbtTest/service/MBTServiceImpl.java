package com.dbproject.api.mbtTest.service;

import com.dbproject.api.city.City;
import com.dbproject.api.explore.CityDto;
import com.dbproject.api.mbtTest.MBTCityDto;
import com.dbproject.api.mbtTest.MBTCityDtoCamel;
import com.dbproject.api.mbtTest.repository.MBTMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Qualifier("MBTServiceImpl")
public class MBTServiceImpl implements MBTService {

    @Autowired
    MBTMapper mapper;

    @Override
    public MBTCityDtoCamel getCity() {

        MBTCityDtoCamel mbtCityDto = mapper.selectCity("臺北市");
        mbtCityDto.printDetail();

        MBTCityDtoCamel mbtCityDto2 = mapper.selectCity("臺南市");
        mbtCityDto2.printDetail();

        List<MBTCityDto> allCity = mapper.selectAll();

        for (MBTCityDto cityDto : allCity) {
            cityDto.printDetail();
        }
//        MBTCityDto mbtCityDto = new MBTCityDto(city.getPostalAddressCity(), city.getCityDetail());
//        MBTCityDto mbtCityDto = new MBTCityDto();
        return mbtCityDto2;
    }
}
