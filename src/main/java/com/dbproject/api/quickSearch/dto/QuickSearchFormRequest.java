package com.dbproject.api.quickSearch.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuickSearchFormRequest {

    private String searchQuery;
    private String searchCity;
    private String searchTown;
    private String orderType;
    private String openTimeCond;
    private String feeCond;
    private String picCond;
    private Integer page;

    public QuickSearchFormRequest() {
    }

    public QuickSearchFormRequest(String searchQuery, String searchCity, String searchTown, String orderType, String openTimeCond, String feeCond, String picCond, Integer page) {
        this.searchQuery = searchQuery;
        this.searchCity = searchCity;
        this.searchTown = searchTown;
        this.orderType = orderType;
        this.openTimeCond = openTimeCond;
        this.feeCond = feeCond;
        this.picCond = picCond;
        this.page = page;
    }

    @Override
    public String toString() {
        return "QuickSearchFormRequest{" +
                "searchQuery='" + searchQuery + '\'' +
                ", searchCity='" + searchCity + '\'' +
                ", searchTown='" + searchTown + '\'' +
                ", orderType='" + orderType + '\'' +
                ", openTimeCond='" + openTimeCond + '\'' +
                ", feeCond='" + feeCond + '\'' +
                ", picCond='" + picCond + '\'' +
                '}';
    }
}
