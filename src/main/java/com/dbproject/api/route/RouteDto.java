package com.dbproject.api.route;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RouteDto {

    private Integer routeNum;

//    private List<RouteLocationDto> routeLocationDtoList = new ArrayList<>();

    private String startPointName;
    private String startPointImgUrl;
    private String startPointDescribe;

    private String wayPoint1Name;
    private String wayPoint1ImgUrl;
    private String wayPoint1Describe;

    private String wayPoint2Name;
    private String wayPoint2ImgUrl;
    private String wayPoint2Describe;

    private String arrivePointName;
    private String arrivePointImgUrl;
    private String arrivePointDescribe;


}
