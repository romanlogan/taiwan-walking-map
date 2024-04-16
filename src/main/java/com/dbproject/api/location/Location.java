package com.dbproject.api.location;

import com.dbproject.api.invitePlan.InvitePlan;

import com.dbproject.api.route.Route;
import com.dbproject.exception.CommentCountLessThanOrEqualZeroException;
import com.dbproject.exception.CommentCountNullException;
import com.dbproject.exception.FavoriteCountLessThanOrEqualZeroException;
import com.dbproject.exception.FavoriteCountNullException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "location")
@Getter
@Setter
public class Location {

    @Id
    private String locationId;

    private String name;

    // 댓글 개수
    private Integer commentCount;

    // 즐겨찾기 개수
    private Integer favoriteCount;


    private String zone;

    private String tolDescribe;

    private String description;

    private String tel;

    private String address;

    private String zipcode;

    private String region;

    private String town;

    private String travellingInfo;

    private String openTime;

    @Embedded
    private LocationPicture locationPicture;

//    private String picture1;
//
//    private String picDescribe1;
//
//    private String picture2;
//
//    private String picDescribe2;
//
//    private String picture3;
//
//    private String picDescribe3;

    private String map;

    private String gov;

    private String longitude;

    private String latitude;

    @Embedded
    private LocationClass locationClass;

//    private String orgclass;
//
//    private String class1;
//
//    private String class2;
//
//    private String class3;

    private String level;

    private String website;

    private String parkingInfo;

    private String parkingInfoPx;

    private String parkingInfoPy;

    private String ticketInfo;

    private String remarks;

    private String keyword;

    private String changeTime;

    @ManyToOne
    @JoinColumn(name = "invite_plan_id")
    private InvitePlan invitePlan;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

//    @OneToMany(mappedBy = "location")
//    private List<PlanLocation> planList;      //일대다 단방향대신 다대일 양방향을 사용하기 위해 추가 , Location 에 외래키 추가 필요

    public LocationPicture getLocationPictureMethod() {
        return this.locationPicture;
    }

    public void increaseCommentCount() {

        if (this.getCommentCount() == null) {
            this.commentCount = 1;
        }else{
            this.commentCount++;
        }
    }

    public void decreaseCommentCount() {

        if (this.getCommentCount() == null) {
            throw new CommentCountNullException("현재 댓글 개수가 null 입니다 (delete)");
        } else if (this.getCommentCount() <= 0) {
            throw new CommentCountLessThanOrEqualZeroException("현재 댓글 개수가 0 이하입니다 (delete)");
        } else{
            this.commentCount--;
        }
    }

    public void increaseFavoriteCount() {

        if (this.getFavoriteCount() == null) {
            this.favoriteCount = 1;
        }else{
            this.favoriteCount++;
        }
    }

    public void decreaseFavoriteCount() {

        if (this.getFavoriteCount() == null) {
            throw new FavoriteCountNullException("현재 즐겨찾기 개수가 null 입니다 (delete)");
        } else if (this.getFavoriteCount() <= 0) {
            throw new FavoriteCountLessThanOrEqualZeroException("현재 즐겨찾기 개수가 0 이하입니다 (delete)");
        } else{
            this.favoriteCount--;
        }
    }
}




















