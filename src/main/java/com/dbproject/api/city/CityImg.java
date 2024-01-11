package com.dbproject.api.city;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "city_img")
@Getter
@Setter
public class CityImg {

    @Id
    private Integer id;

    private String postalAddressCity;

    private String imgUrl;

    private String imgName;

    private String repImgYn;
}
