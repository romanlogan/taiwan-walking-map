package com.dbproject.api.quickSearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
public class QuickSearchListResponse extends QuickSearchResponse {

    //    query projection 이 달려있는 dto 를 공유 가능할까 ?
    List<QuickSearchLocationDto> quickSearchLocationDtoList = new ArrayList<>();

}
