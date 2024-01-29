package com.dbproject.api.mbtTest.service;


import com.dbproject.api.mbtTest.MBTCityDto;
import org.springframework.stereotype.Service;

@Service
public interface MBTService {

    public MBTCityDto getCity();
}
