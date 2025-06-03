package com.dbproject.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * locationId 是 C1_371020000A_000425  = 20字
 * content 是 最多 255字
 * rate 1點 ～ 5點
 */
@Getter
@Setter
public class CreateCommentRequest {

    @NotNull(message = "locationId value is required")
    @Length(min = 20, max = 20, message = "locationId requires 20 characters")
    private String locationId;

    @NotBlank(message = "content value is required")
    @Length(max = 255, message = "content value can only be up to 255 words")
    private String content;

    @NotNull(message = "rate value is required")
    @Range(min = 1, max = 5, message = "rate can only have values from 1 to 5 points")
    private Integer rate;

    public CreateCommentRequest() {
    }

    @Builder
    public CreateCommentRequest(String locationId, String content, Integer rate) {
        this.locationId = locationId;
        this.content = content;
        this.rate = rate;
    }

    public static CreateCommentRequest of(String locationId, String content, Integer rate) {

        return CreateCommentRequest.builder()
                .locationId(locationId)
                .content(content)
                .rate(rate)
                .build();
    }


}
