package com.dbproject.repository;

import com.dbproject.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryCustom {

    Page<Location> getLocationPageByCity(String arriveCity, Pageable pageable);
}
