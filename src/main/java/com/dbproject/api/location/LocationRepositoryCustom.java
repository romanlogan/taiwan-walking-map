package com.dbproject.api.location;

import com.dbproject.api.quickSearch.dto.FastSearchDto;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
import com.dbproject.api.quickSearch.dto.QuickSearchLocationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationRepositoryCustom {

    Page<RecLocationListResponse> getLocationPageByCity(RecLocationListRequest request, Pageable pageable);

    Page<QuickSearchLocationDto> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable);

    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable);

    List<QuickSearchLocationDto> getLocationListByCond(QuickSearchFormRequest quickSearchFormRequest,
                                                       Pageable pageable);
}
