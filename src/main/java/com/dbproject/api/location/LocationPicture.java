package com.dbproject.api.location;


import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class LocationPicture {

    private String picture1;

    private String picDescribe1;

    private String picture2;

    private String picDescribe2;

    private String picture3;

    private String picDescribe3;

    public String getPicture1() {
        return this.picture1;
    }
    
//    setter 를 없애서 불변 객체로 만ㄹ들어야함 or private 으로 만들기
    private void setPicture1(String picture1) {
        this.picture1 = picture1;
    }


}
