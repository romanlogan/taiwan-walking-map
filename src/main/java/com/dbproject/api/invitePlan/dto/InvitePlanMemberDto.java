package com.dbproject.api.invitePlan.dto;

import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.member.Member;
import com.dbproject.constant.InvitePlanStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter
@Setter
public class InvitePlanMemberDto {

    private String email;
    private String name;
    private String nickName;
    private String gender;
    private InvitePlanStatus invitePlanStatus;
    private String supply;      //누가 가져가야할지 정해진 준비


    private static ModelMapper mapper = new ModelMapper();

    public static InvitePlanMemberDto of(Member member) {

        return mapper.map(member, InvitePlanMemberDto.class);
    }

    public InvitePlanMemberDto() {
    }

    @Builder
    public InvitePlanMemberDto(String email, String name, String nickName, String gender, InvitePlanStatus invitePlanStatus, String supply) {
        this.email = email;
        this.name = name;
        this.nickName = nickName;
        this.gender = gender;
        this.invitePlanStatus = invitePlanStatus;
        this.supply = supply;
    }

    public static InvitePlanMemberDto from(InvitePlanMember invitePlanMember) {

        return InvitePlanMemberDto.builder()
                .email(invitePlanMember.getMember().getEmail())
                .name(invitePlanMember.getMember().getName())
                .nickName(invitePlanMember.getMember().getNickName())
                .gender(invitePlanMember.getMember().getGender())
                .invitePlanStatus(invitePlanMember.getStatus())
                .supply(invitePlanMember.getSupply())
                .build();
    }

}
