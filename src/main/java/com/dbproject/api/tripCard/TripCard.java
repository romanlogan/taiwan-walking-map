package com.dbproject.api.tripCard;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tripcard")
@Getter
@Setter
public class TripCard {

    @Id
    private Integer num;
    private String area;
    private String serviceCenter;
    private String serviceTime;
    private String phone;
    private String address;
    private String addressDetail;

    @Override
    public String toString() {
        return "TripCard{" +
                "num=" + num +
                ", area='" + area + '\'' +
                ", serviceCenter='" + serviceCenter + '\'' +
                ", serviceTime='" + serviceTime + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address  + '\'' +
                ", addressDetail='" + addressDetail + '\'' +
                '}';
    }
}
