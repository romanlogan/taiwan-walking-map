package com.dbproject.api.city;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CityRepository extends JpaRepository<City, String> {

    //    select * from Location where Location.postalAdressCity = "台中市"
    @Query("select c from City c join fetch c.cityImg where c.region = :region")
    City findByRegion(String region);


}
