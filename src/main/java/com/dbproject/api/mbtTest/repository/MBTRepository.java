package com.dbproject.api.mbtTest.repository;

import com.dbproject.api.city.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MBTRepository extends JpaRepository<City, String> {


    City findBypostalAddressCity(String postalAddressCity);

}
