package com.dbproject.api.mbtTest.repository;

import com.dbproject.api.city.City;

public interface MBTRepoInt {

    City findBypostalAddressCity(String postalAddressCity);
}
