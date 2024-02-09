package com.dbproject.web.comment;

import com.dbproject.api.comment.CommentService;
import com.dbproject.api.comment.CreateCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

        commentService.checkDuplicateCreateComment(createCommentRequest,principal.getName());
        Long commentId = commentService.createComment(createCommentRequest, principal.getName());

        return new ResponseEntity(commentId, HttpStatus.OK);
    }

    @PutMapping("/updateComment")
    public ResponseEntity updateComment(@Valid @RequestBody UpdateCommentRequest updateCommentRequest,
                                        Principal principal) {

        commentService.updateComment(updateCommentRequest, principal.getName());

        return new ResponseEntity(1L, HttpStatus.OK);
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity deleteComment(@RequestBody String commentId,
                                        Principal principal) {

        commentService.deleteComment(commentId);


        return new ResponseEntity(1L, HttpStatus.OK);
    }
}
