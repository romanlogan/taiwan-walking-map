package com.dbproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCommentRequest {

    private String locationId;
    private String content;
    private Integer rate;
}
