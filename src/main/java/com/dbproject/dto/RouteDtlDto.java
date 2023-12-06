package com.dbproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RouteDtlDto {

    private StartPointDto startPointDto;
    private WayPoint1Dto wayPoint1Dto;
    private WayPoint2Dto wayPoint2Dto;
    private ArrivePointDto arrivePointDto;

}
