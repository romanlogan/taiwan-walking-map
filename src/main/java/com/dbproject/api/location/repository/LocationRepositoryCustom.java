package com.dbproject.api.location.repository;

import com.dbproject.api.location.Location;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.location.dto.RecommendLocationListRequest;
import com.dbproject.api.location.dto.RecommendLocationDto;
import com.dbproject.api.quickSearch.dto.FastSearchDto;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
import com.dbproject.api.quickSearch.dto.QuickSearchLocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationRepositoryCustom {

    Page<RecommendLocationDto> getLocationPageByCity(RecommendLocationListRequest request, Pageable pageable);

    Page<QuickSearchLocationDto> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable);

    Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable);

    List<QuickSearchLocationDto> getLocationListByCond(QuickSearchFormRequest quickSearchFormRequest,
                                                       Pageable pageable);
    List<LocationDto> findTop10RecommendLocationList(String region);
}
