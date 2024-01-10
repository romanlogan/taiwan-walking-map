package com.dbproject.entity;


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

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        LocationPicture that = (LocationPicture) o;
//        return Objects.equals(picture1, that.picture1) &&
//                Objects.equals(picDescribe1, that.picDescribe1) && Objects.equals(picture2, that.picture2) && Objects.equals(picDescribe2, that.picDescribe2) && Objects.equals(picture3, that.picture3) && Objects.equals(picDescribe3, that.picDescribe3);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(picture1, picDescribe1, picture2, picDescribe2, picture3, picDescribe3);
//    }
}
