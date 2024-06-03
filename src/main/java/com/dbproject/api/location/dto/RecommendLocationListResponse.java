package com.dbproject.api.location.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class RecommendLocationListResponse {


    private Page<RecommendLocationDto> locationDtoPage;
    private SearchRequestConditionDto searchConditionDto;
    private List<String> townList;

    public RecommendLocationListResponse() {
    }


    @Builder
    public RecommendLocationListResponse(Page<RecommendLocationDto> locationDtoPage, SearchRequestConditionDto searchConditionDto, List<String> townList) {
        this.locationDtoPage = locationDtoPage;
        this.searchConditionDto = searchConditionDto;
        this.townList = townList;
    }

    public static RecommendLocationListResponse create(Page<RecommendLocationDto> locationDtoPage, SearchRequestConditionDto searchConditionDto, List<String> townList) {

        return RecommendLocationListResponse.builder()
                .locationDtoPage(locationDtoPage)
                .searchConditionDto(searchConditionDto)
                .townList(townList)
                .build();
    }

}
