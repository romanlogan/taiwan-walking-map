package com.dbproject.repository;

import com.dbproject.entity.CityImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityImgRepository extends JpaRepository<CityImg, Integer> {

    List<CityImg> findByPostalAddressCity(String postalAddressCity);
}
