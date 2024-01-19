package com.dbproject.api.myPage;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class DeleteMemberRequest {

    @NotEmpty
    private String email;
}
