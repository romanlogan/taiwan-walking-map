package com.dbproject.api.location;

import com.dbproject.web.quickSearch.FastSearchDto;
import com.dbproject.web.quickSearch.QuickSearchLocationDto;
import com.dbproject.web.location.RecLocationListRequest;
import com.dbproject.web.location.RecLocationListResponse;
import com.dbproject.api.location.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryCustom {

    Page<RecLocationListResponse> getLocationPageByCity(RecLocationListRequest request, Pageable pageable);

    Page<QuickSearchLocationDto> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable);

    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable);
}
