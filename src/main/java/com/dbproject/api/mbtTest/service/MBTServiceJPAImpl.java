package com.dbproject.api.mbtTest.service;

import com.dbproject.api.city.City;
import com.dbproject.api.mbtTest.MBTCityDtoCamel;
import com.dbproject.api.mbtTest.repository.MBTRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//@RequiredArgsConstructor
@Qualifier("MBTServiceJPAImpl")
public class MBTServiceJPAImpl implements MBTService{

//    @Autowired
    private MBTRepository mbtRepository;

    @Autowired
    public MBTServiceJPAImpl(MBTRepository mbtRepository) {
        this.mbtRepository = mbtRepository;
    }

    @Override
    public MBTCityDtoCamel getCity() {

        System.out.println("-------------jpa------------");
        String cityName = "臺北市";
        City city = mbtRepository.findByRegion(cityName);

        MBTCityDtoCamel cityDto = new MBTCityDtoCamel(city.getRegion(), city.getCityDetail(), city.getPositionLat(), city.getPositionLon());
        cityDto.printDetail();
        return cityDto;
    }

}
