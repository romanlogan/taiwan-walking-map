package com.dbproject.api.comment.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommentRequest {


    @NotBlank(message = "locationId 는 필수 값 입니다.")
    private String locationId;

    @NotBlank(message = "content 는 필수 값 입니다.")
    private String content;

    @NotNull(message = "rate 는 필수 값 입니다.")
    private Integer rate;

    public UpdateCommentRequest() {
    }

    public UpdateCommentRequest(String locationId, String content, Integer rate) {
        this.locationId = locationId;
        this.content = content;
        this.rate = rate;
    }
}
