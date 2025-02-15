package com.dbproject.api.comment.service;

import com.dbproject.api.comment.dto.*;

public interface CommentService {

    Long createComment(CreateCommentRequest createCommentRequest, String email);
    Long updateComment(UpdateCommentRequest updateCommentRequest, String email);
    void deleteComment(DeleteCommentRequest deleteCommentRequest);
    void checkDuplicateCreateComment(CreateCommentRequest createCommentRequest, String email);
    GetNextCommentListResponse getNextCommentList(GetNextCommentListRequest request);
}
