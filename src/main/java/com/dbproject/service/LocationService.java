package com.dbproject.service;

import com.dbproject.dto.SearchByCityDto;
import com.dbproject.entity.Location;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location getLocationDtl(String locationId) {

        Location location = locationRepository.findByLocationId(locationId);

        return location;
    }

    public Page<Location> getLocationPageByCity(SearchByCityDto searchByCityDto, Pageable pageable) {

        Page<Location> locationList = locationRepository.getLocationPageByCity(searchByCityDto.getArriveCity(), pageable);

        return locationList;
    }
}
