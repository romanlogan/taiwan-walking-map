package com.dbproject.api.location.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class RecommendLocationListResponse {


    private Page<RecommendLocationDto> locationDtoPage;
    private SearchConditionDto searchConditionDto;
    private List<String> townList;

    public RecommendLocationListResponse() {
    }

    public RecommendLocationListResponse(Page<RecommendLocationDto> locationDtoPage, SearchConditionDto searchConditionDto, List<String> townList) {
        this.locationDtoPage = locationDtoPage;
        this.searchConditionDto = searchConditionDto;
        this.townList = townList;
    }
}
