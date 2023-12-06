package com.dbproject.repository;

import com.dbproject.dto.FastSearchDto;
import com.dbproject.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryCustom {

    Page<Location> getLocationPageByCity(String arriveCity, Pageable pageable);

    Page<Location> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable);

    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable);
}
