package com.dbproject.api.comment.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UpdateCommentRequest {

    @NotNull(message = "locationId值是必要")
    @Length(min = 20, max = 20, message = "locationId要20個字")
    private String locationId;

    @NotBlank(message = "content值是必要")
    @Length(max = 255, message = "content只能最多255字")
    private String content;

    @NotNull(message = "rate值是必要")
    @Range(min = 1, max = 5, message = "rate只能從1點到五點的值")
    private Integer rate;

    public UpdateCommentRequest() {
    }

    public UpdateCommentRequest(String locationId, String content, Integer rate) {
        this.locationId = locationId;
        this.content = content;
        this.rate = rate;
    }
}
