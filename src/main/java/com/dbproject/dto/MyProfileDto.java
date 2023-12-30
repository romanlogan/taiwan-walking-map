package com.dbproject.dto;

import com.dbproject.constant.Role;
import com.dbproject.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class MyProfileDto {

    private Long id;

    private String name;

    private String email;

    private String address;

    private Role role;

    public MyProfileDto() {
    }

    @Builder
    private MyProfileDto(Long id,String name, String email, String address, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.address = address;
        this.role = role;
    }

    public static MyProfileDto of(Member member) {

        return MyProfileDto.builder()
                .id(member.getId())
                .name(member.getName())
                .email(member.getEmail())
                .address(member.getAddress())
                .role(member.getRole())
                .build();
    }
}
