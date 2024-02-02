package com.dbproject.api.quickSearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class QuickSearchResponse {


    private QuickSearchCityDto city;

    private String selectedTown;

    private List<String> cityNameList = new ArrayList<>();

    private List<String> townNameList = new ArrayList<>();





}
