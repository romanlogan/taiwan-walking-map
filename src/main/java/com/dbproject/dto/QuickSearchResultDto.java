package com.dbproject.dto;

import com.dbproject.entity.City;
import com.dbproject.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class QuickSearchResultDto {

//    dto 안에 엔티티도 변경 필요

    private Page<QuickSearchLocationDto> locationPage;
    private QuickSearchCityDto city;

}
