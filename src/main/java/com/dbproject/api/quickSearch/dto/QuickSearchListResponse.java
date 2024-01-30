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



    //city 정보 (다른 도시 선택시 지도의 이니셜 위치 옮기기)

    //장소 정보 (장소 디테일 표시)


}
