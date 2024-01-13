package com.dbproject.web.member;

import com.dbproject.constant.Role;
import com.dbproject.api.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyProfileDto {

    private String name;

    private String email;

    private String address;

    private Role role;

    public MyProfileDto() {
    }

    @Builder
    private MyProfileDto(String name, String email, String address, Role role) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
    }

    public static MyProfileDto of(Member member) {

        return MyProfileDto.builder()
                .name(member.getName())
                .email(member.getEmail())
                .address(member.getAddress())
                .role(member.getRole())
                .build();
    }
}
