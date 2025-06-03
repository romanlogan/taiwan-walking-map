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
    @Column(name="city_id")
    private Long id;

    @Column(nullable = false, length = 10)
    private String region;

    @Column(length = 200)
    private String cityDetail;

    @Column(name = "PositionLat",nullable = false)
    private BigDecimal PositionLat;

    @Column(name = "PositionLon",nullable = false)
    private BigDecimal PositionLon;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="city_img_id")
    private CityImg cityImg;

}
