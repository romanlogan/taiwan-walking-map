package com.dbproject.service;

import com.dbproject.entity.Location;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    public Location getLocationDtl(String attractionId) {

        Location location = locationRepository.findByAttractionId(attractionId);

        return location;
    }
}
