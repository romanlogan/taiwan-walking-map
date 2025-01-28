package com.dbproject.api.mbtTest.repository;

import com.dbproject.api.city.City;

public interface MBTRepoInt {

    City findByRegion(String postalAddressCity);
}
