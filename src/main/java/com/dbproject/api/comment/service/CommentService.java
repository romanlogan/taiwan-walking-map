package com.dbproject.api.comment.service;

import com.dbproject.api.comment.dto.CreateCommentRequest;
import com.dbproject.api.comment.dto.DeleteCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;

public interface CommentService {

    Long createComment(CreateCommentRequest createCommentRequest, String email);
    Long updateComment(UpdateCommentRequest updateCommentRequest, String email);
    void deleteComment(DeleteCommentRequest deleteCommentRequest);
    void checkDuplicateCreateComment(CreateCommentRequest createCommentRequest, String email);
}
