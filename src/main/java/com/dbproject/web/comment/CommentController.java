package com.dbproject.web.comment;

import com.dbproject.api.comment.CommentService;
import com.dbproject.api.comment.CreateCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest,
                                        Principal principal) {


        System.out.println("start~~~~");
        commentService.checkDuplicateCreateComment(createCommentRequest,principal.getName());

        System.out.println("after checkDup~~~~");
        Long commentId = commentService.createComment(createCommentRequest, principal.getName());

        System.out.println("saved");
        return new ResponseEntity(commentId, HttpStatus.OK);

    }


}
