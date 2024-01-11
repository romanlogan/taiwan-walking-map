package com.dbproject.api.city;

import com.dbproject.api.city.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, String> {


    //    select * from Location where Location.postalAdressCity = "台中市"
          City findBypostalAddressCity(String postalAddressCity);


}
