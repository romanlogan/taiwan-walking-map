package com.dbproject.dto;

import com.dbproject.entity.City;
import com.dbproject.entity.Location;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class QuickSearchResultDto {

    private Page<Location> locationPage;
    private City city;

}
