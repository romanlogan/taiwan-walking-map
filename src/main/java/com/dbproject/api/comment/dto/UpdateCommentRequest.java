package com.dbproject.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommentRequest {

    @NotNull(message = "locationId value is required")
    @Length(min = 20, max = 20, message = "locationId requires 20 characters")
    private String locationId;

    @NotBlank(message = "content value is required")
    @Length(max = 255, message = "content value can only be up to 255 words")
    private String content;

    @NotNull(message = "rate value is required")
    @Range(min = 1, max = 5, message = "rate can only have values from 1 to 5 points")
    private Integer rate;

    public UpdateCommentRequest() {
    }

    @Builder
    public UpdateCommentRequest(String locationId, String content, Integer rate) {
        this.locationId = locationId;
        this.content = content;
        this.rate = rate;
    }

    public static UpdateCommentRequest create(String locationId, String content, Integer rate) {

        return UpdateCommentRequest.builder()
                .locationId(locationId)
                .content(content)
                .rate(rate)
                .build();
    }
}
