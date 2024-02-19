package com.dbproject.api.mbtTest.service;


import com.dbproject.api.mbtTest.MBTCityDto;
import com.dbproject.api.mbtTest.MBTCityDtoCamel;
import org.springframework.stereotype.Service;

@Service
public interface MBTService {

    public MBTCityDtoCamel getCity();
}
