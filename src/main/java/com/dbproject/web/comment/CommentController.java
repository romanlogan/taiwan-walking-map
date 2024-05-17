package com.dbproject.web.comment;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.comment.service.CommentService;
import com.dbproject.api.comment.dto.CreateCommentRequest;
import com.dbproject.api.comment.dto.DeleteCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import com.dbproject.binding.CheckBindingResult;
import com.dbproject.exception.CommentNotExistException;
import com.dbproject.exception.DuplicateCreateCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/createComment")
    public ResponseEntity createComment(@Valid @RequestBody CreateCommentRequest createCommentRequest,
                                        BindingResult bindingResult,
                                        Principal principal) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        Long commentId = null;

        try {
            commentId = commentService.createComment(createCommentRequest, principal.getName());
        } catch (DuplicateCreateCommentException e) {
            return null;
        } catch (CommentNotExistException e) {
            return null;
        }

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                List.of(commentId),
                null
        ), HttpStatus.OK);
    }

    @PutMapping("/updateComment")
    public ResponseEntity updateComment(@Valid @RequestBody UpdateCommentRequest updateCommentRequest,
                                        BindingResult bindingResult,
                                        Principal principal) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
            return responseEntity;
        }

        try {
            commentService.updateComment(updateCommentRequest, principal.getName());
        } catch (CommentNotExistException e) {
            return null;
        }

//        return new ResponseEntity(1L, HttpStatus.OK);
        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                null,
                null
        ), HttpStatus.OK);
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity deleteComment(@Valid @RequestBody DeleteCommentRequest deleteCommentRequest,
                                        BindingResult bindingResult,
                                        Principal principal) {

        if(bindingResult.hasErrors()){
            ResponseEntity responseEntity = CheckBindingResult.induceErrorInAjax(bindingResult);
            return responseEntity;
        }

        try {
            commentService.deleteComment(deleteCommentRequest);
        } catch (CommentNotExistException e) {
            return null;
        }

//        return new ResponseEntity(1L, HttpStatus.OK);
        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                null,
                null
        ), HttpStatus.OK);
    }
}
