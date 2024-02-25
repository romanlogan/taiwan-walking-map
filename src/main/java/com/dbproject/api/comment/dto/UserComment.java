package com.dbproject.api.comment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserComment {

    private String content;

    private Integer rate;

    private String memberEmail;

    private String locationId;

    public UserComment(String content, Integer rate, String memberEmail, String locationId) {
        this.content = content;
        this.rate = rate;
        this.memberEmail = memberEmail;
        this.locationId = locationId;
    }
}
