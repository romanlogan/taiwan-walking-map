package com.dbproject.api.quickSearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class QuickSearchPageResponse extends QuickSearchResponse {

//    dto 안에 엔티티도 변경 필요

    private Page<QuickSearchLocationDto> locationPage;
//    private QuickSearchCityDto city;

}
