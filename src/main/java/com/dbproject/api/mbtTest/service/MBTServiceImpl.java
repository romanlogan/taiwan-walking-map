package com.dbproject.api.mbtTest.service;

import com.dbproject.api.mbtTest.MBTCityDtoCamel;
import com.dbproject.api.mbtTest.repository.MBTMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
//@RequiredArgsConstructor
@Qualifier("MBTServiceImpl")
public class MBTServiceImpl implements MBTService {

    @Autowired
    private MBTMapper mapper;

//    public MBTServiceImpl(MBTMapper mapper) {
//        this.mapper = mapper;
//    }

    @Override
    public MBTCityDtoCamel getCity() {

//        List<MBTCityDto> allCity = mapper.selectAll();
//
//        for (MBTCityDto cityDto : allCity) {
//            cityDto.printDetail();
//        }
        System.out.println("-------------mybatis------------");

        MBTCityDtoCamel mbtCityDto = mapper.selectCity("臺北市");
        mbtCityDto.printDetail();

        MBTCityDtoCamel mbtCityDto2 = mapper.selectCity("臺南市");
        mbtCityDto2.printDetail();

//        MBTCityDto mbtCityDto = new MBTCityDto(city.getPostalAddressCity(), city.getCityDetail());
//        MBTCityDto mbtCityDto = new MBTCityDto();
        return mbtCityDto2;
    }
}
