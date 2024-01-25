package com.dbproject.api.city;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public void getCity() {

        City city = cityRepository.findById("高雄市").orElseThrow(EntityNotFoundException::new);

        System.out.println(city.getPostalAddressCity() + " " + city.getCityDetail());
    }
}