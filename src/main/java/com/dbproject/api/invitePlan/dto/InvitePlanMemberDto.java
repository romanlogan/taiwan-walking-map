package com.dbproject.api.invitePlan.dto;

import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
@NoArgsConstructor
public class InvitePlanMemberDto {

    private String email;
    private String name;
    private String nickName;
    private Integer gender;

    private static ModelMapper mapper = new ModelMapper();

    public static InvitePlanMemberDto of(Member member) {

        return mapper.map(member, InvitePlanMemberDto.class);

    }

    public InvitePlanMemberDto(String email, String name, String nickName, Integer gender) {
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
    }
}
