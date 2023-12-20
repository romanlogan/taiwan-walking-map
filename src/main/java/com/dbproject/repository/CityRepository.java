package com.dbproject.repository;

import com.dbproject.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, String> {


    //    select * from Location where Location.postalAdressCity = "台中市"
          City findBypostalAddressCity(String postalAddressCity);


}
