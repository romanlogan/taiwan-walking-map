package com.dbproject.api.member.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DeleteMemberRequest {

    @NotEmpty
    private String email;
}
