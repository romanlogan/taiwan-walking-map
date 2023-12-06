package com.dbproject.dto;

import com.dbproject.entity.Location;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LocationListDto {

    List<Location> locationList = new ArrayList<>();

    String errorMessage;

}
