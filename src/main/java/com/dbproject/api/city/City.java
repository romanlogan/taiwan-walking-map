package com.dbproject.api.city;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "city")
@Getter
@Setter
@ToString
public class City {

    @Id
    private String postalAddressCity;

    private String cityDetail;

    @Column(name = "PositionLat")
    private BigDecimal PositionLat;

    @Column(name = "PositionLon")
    private BigDecimal PositionLon;



}
