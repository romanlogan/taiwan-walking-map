package com.dbproject.api.comment.service;

import com.dbproject.api.comment.dto.CreateCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;

public interface CommentService {

    Long createComment(CreateCommentRequest createCommentRequest, String email);
    void updateComment(UpdateCommentRequest updateCommentRequest, String email);
    void deleteComment(Integer commentId);
    void checkDuplicateCreateComment(CreateCommentRequest createCommentRequest, String email);
}
