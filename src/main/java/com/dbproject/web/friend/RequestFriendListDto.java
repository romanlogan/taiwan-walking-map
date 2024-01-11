package com.dbproject.web.friend;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestFriendListDto {

    private String email;

    private String name;


    public RequestFriendListDto() {

    }

    @QueryProjection
    public RequestFriendListDto(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
