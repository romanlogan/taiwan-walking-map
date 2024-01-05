package com.dbproject.controller;

import com.dbproject.dto.CreateCommentRequest;
import com.dbproject.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity createComment(@RequestBody CreateCommentRequest createCommentRequest) {


        Long commentId = commentService.createComment(createCommentRequest);

        return new ResponseEntity(commentId, HttpStatus.OK);

    }


}
