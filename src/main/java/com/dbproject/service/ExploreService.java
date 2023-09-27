package com.dbproject.service;

import com.dbproject.dto.LocationListDto;
import com.dbproject.dto.SearchByCityDto;
import com.dbproject.entity.Location;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ExploreService {

    private final LocationRepository locationRepository;

    public Page<Location> getLocationPageByCity(SearchByCityDto searchByCityDto, Pageable pageable) {


        Page<Location> locationList = locationRepository.getLocationPageByCity(searchByCityDto.getArriveCity(), pageable);



        return locationList;

    }

}
