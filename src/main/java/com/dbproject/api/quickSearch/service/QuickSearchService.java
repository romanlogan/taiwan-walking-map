package com.dbproject.api.quickSearch.service;

import com.dbproject.api.quickSearch.dto.FastSearchDto;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
import com.dbproject.api.quickSearch.dto.QuickSearchListResponse;
import com.dbproject.api.quickSearch.dto.QuickSearchPageResponse;
import org.springframework.data.domain.Pageable;

public interface QuickSearchService {

    QuickSearchListResponse getQuickSearchList(FastSearchDto fastSearchDto, Pageable pageable);
    QuickSearchPageResponse getQuickSearchPage(FastSearchDto fastSearchDto, Pageable pageable);
    QuickSearchListResponse getQuickSearchListByCond(QuickSearchFormRequest quickSearchFormRequest, Pageable pageable);
    void findLocationList(QuickSearchListResponse quickSearchListResponse ,String searchQuery, String searchCity, Pageable pageable);
    void findLocationPage(QuickSearchPageResponse quickSearchPageResponse ,FastSearchDto fastSearchDto, Pageable pageable);
}
