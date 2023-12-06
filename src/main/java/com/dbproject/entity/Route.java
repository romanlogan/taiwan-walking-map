package com.dbproject.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "route")
@Getter
@Setter
public class Route {

    @Id
    private Integer id;
    private String postalAddressCity;
    private String startPoint;
    private String wayPoint1;
    private String wayPoint2;
    private String arrivePoint;

}
