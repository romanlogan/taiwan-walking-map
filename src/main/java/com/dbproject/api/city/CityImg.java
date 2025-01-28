package com.dbproject.api.city;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "city_img")
@Getter
@Setter
public class CityImg {

    @Id
    @Column(name="city_img_id")
    private Integer id;

    @Column(length = 10)
    private String region;

    @Column(length = 255)
    private String imgUrl;

    @Column(length = 50)
    private String imgName;

    @Column(length = 1)
    private String repImgYn;

//    @OneToOne(mappedBy = "city")
//    private City city;
}
