package com.dbproject.service;

import com.dbproject.dto.LocationDtlResponse;
import com.dbproject.dto.SearchByCityDto;
import com.dbproject.entity.FavoriteLocation;
import com.dbproject.entity.Location;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.repository.FavoriteRepository;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

    private final FavoriteRepository favoriteRepository;


    public LocationDtlResponse getLocationDtl(String locationId) {

        Location location = locationRepository.findByLocationId(locationId);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        return locationDtlResponse;
    }

    public LocationDtlResponse getLocationDtlWithAuthUser(String locationId,String email) {

        Location location = locationRepository.findByLocationId(locationId);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        checkSavedFavoriteLocation(location,locationDtlResponse);

        return locationDtlResponse;
    }



    private void checkSavedFavoriteLocation(Location location, LocationDtlResponse locationDtlResponse) {

        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId(location.getLocationId());

        if (favoriteLocation == null) {
            // 기본값이 false 니까 이건 안쓰는게 맞을까 ?
            locationDtlResponse.setNotSaved();
        }else{
            locationDtlResponse.setSaved();
        }

    }

    public Page<Location> getLocationPageByCity(SearchByCityDto searchByCityDto, Pageable pageable) {

        Page<Location> locationList = locationRepository.getLocationPageByCity(searchByCityDto.getArriveCity(), pageable);

        return locationList;
    }
}
