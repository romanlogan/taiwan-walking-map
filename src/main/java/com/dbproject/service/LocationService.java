package com.dbproject.service;

import com.dbproject.dto.LocationDto;
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

    public void locationInfo() {

        Location location = locationRepository.findById("Attraction_371020000A_000386").orElseThrow(EntityNotFoundException::new);
        System.out.println(location.toString());

        LocationDto locationDto = LocationDto.of(location);

        System.out.println(locationDto.toString());
    }
}
