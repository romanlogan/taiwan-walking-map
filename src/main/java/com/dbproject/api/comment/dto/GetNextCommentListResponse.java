package com.dbproject.api.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetNextCommentListResponse {

    private List<CommentDto> commentDtoList;

    public GetNextCommentListResponse() {
    }

    public GetNextCommentListResponse(List<CommentDto> commentDtoList) {
        this.commentDtoList = commentDtoList;
    }
}
