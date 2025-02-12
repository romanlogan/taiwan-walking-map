package com.dbproject.api.comment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DeleteCommentRequest {

    @NotNull(message = "commengId值是必要")
    @Min(value = 1,message = "commentId不能低於1")
    private Integer commentId;

    @Builder
    public DeleteCommentRequest(Integer commentId) {
        this.commentId = commentId;
    }

    public static DeleteCommentRequest create(Integer commentId) {

        return DeleteCommentRequest.builder()
                .commentId(commentId)
                .build();
    }

}
