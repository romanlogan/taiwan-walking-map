package com.dbproject.service;

import com.dbproject.dto.CreateCommentRequest;
import com.dbproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Long createComment(CreateCommentRequest createCommentRequest) {




        return 1L;

    }


}
