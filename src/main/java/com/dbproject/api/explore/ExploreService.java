package com.dbproject.api.explore;

import com.dbproject.api.city.dto.CityDto;

public interface ExploreService {

     CityDto getLocationDtl(String cityName);
}
