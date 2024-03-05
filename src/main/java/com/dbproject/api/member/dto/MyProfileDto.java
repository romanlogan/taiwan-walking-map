package com.dbproject.api.member.dto;

import com.dbproject.api.member.memberImg.MemberImg;
import com.dbproject.constant.Role;
import com.dbproject.api.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
public class MyProfileDto {

    private String name;

    private String email;

    private String address;

    private String imgUrl;

    private Role role;

    public MyProfileDto() {
    }


    @Builder
    public MyProfileDto(String name, String email, String address, String imgUrl, Role role) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.imgUrl = imgUrl;
        this.role = role;
    }

    public static MyProfileDto from(Member member, Optional<MemberImg> memberImg) {

        String memberImgUrl;
        if (memberImg.isPresent()) {
            memberImgUrl = memberImg.get().getImgUrl();
        }else{
            memberImgUrl = "";
        }

        return MyProfileDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .address(member.getAddress())
                .role(member.getRole())
                .imgUrl(memberImgUrl)
                .build();
    }
}
