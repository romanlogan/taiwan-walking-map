package com.dbproject.api.comment;

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

    @NotNull(message = "locationId值是必要")
    @Length(min = 20, max = 20, message = "locationId要20個字")  //여기서 Blank 체크 까지 해주는듯
    private String locationId;

    @NotBlank(message = "content值是必要")
    @Length(max = 255, message = "content只能最多255字")
    private String content;

    @NotNull(message = "rate值是必要")
    @Range(min = 1, max = 5, message = "rate只能從1點到五點的值")
    private Integer rate;

    public CreateCommentRequest() {
    }

    public CreateCommentRequest(String locationId, String content, Integer rate) {
        this.locationId = locationId;
        this.content = content;
        this.rate = rate;
    }
}
