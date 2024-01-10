package com.dbproject.repository;

import com.dbproject.dto.FastSearchDto;
import com.dbproject.dto.QuickSearchLocationDto;
import com.dbproject.dto.RecLocationListRequest;
import com.dbproject.dto.RecLocationListResponse;
import com.dbproject.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryCustom {

    Page<RecLocationListResponse> getLocationPageByCity(RecLocationListRequest request, Pageable pageable);

    Page<QuickSearchLocationDto> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable);

    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable);
}
