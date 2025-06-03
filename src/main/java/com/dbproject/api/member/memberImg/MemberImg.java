package com.dbproject.api.member.memberImg;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "member_img")
@Getter
@Setter
public class MemberImg extends BaseEntity {

    @Id
    @Column(name = "member_img_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgName;     //Image file name
    private String oriImgName;      //Original image file name
    private String imgUrl;      //Image lookup path

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="member_email")
    private Member member;

    public MemberImg() {
    }

    public MemberImg(String imgName, String oriImgName, String imgUrl, Member member) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
        this.member = member;
    }

    public void updateMemberImg(String imgName, String oriImgName, String imgUrl) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
        this.imgUrl = imgUrl;
    }
}
