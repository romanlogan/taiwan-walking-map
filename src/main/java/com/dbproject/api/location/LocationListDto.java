package com.dbproject.api.location;

import com.dbproject.api.location.Location;
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
