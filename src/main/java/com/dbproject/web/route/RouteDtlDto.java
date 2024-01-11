package com.dbproject.web.route;

import com.dbproject.web.route.ArrivePointDto;
import com.dbproject.web.route.StartPointDto;
import com.dbproject.web.route.WayPoint1Dto;
import com.dbproject.web.route.WayPoint2Dto;
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
