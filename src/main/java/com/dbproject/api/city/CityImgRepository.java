package com.dbproject.api.city;

import com.dbproject.api.city.CityImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityImgRepository extends JpaRepository<CityImg, Integer> {

    List<CityImg> findByRegion(String region);
}
