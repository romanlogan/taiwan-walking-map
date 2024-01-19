package com.dbproject.api.location;

import com.dbproject.api.quickSearch.FastSearchDto;
import com.dbproject.api.quickSearch.QuickSearchLocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationRepositoryCustom {

    Page<RecLocationListResponse> getLocationPageByCity(RecLocationListRequest request, Pageable pageable);

    Page<QuickSearchLocationDto> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable);

    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable);
}
