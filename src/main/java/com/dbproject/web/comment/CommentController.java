package com.dbproject.web.comment;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.comment.dto.*;
import com.dbproject.api.comment.service.CommentService;
import com.dbproject.binding.CheckBindingResult;
import com.dbproject.exception.CommentNotExistException;
import com.dbproject.exception.DuplicateCreateCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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

    @PostMapping
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

    @PutMapping
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

    @DeleteMapping
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

    @GetMapping("/nextList")
    public ResponseEntity<GetNextCommentListResponse> getNextCommentList(GetNextCommentListRequest request,
                                                                         Principal principal,
                                                                         Model model) {

        String loggedInUserId;

        if (principal == null) {
            //로그인 하지 않은 유저
            loggedInUserId = null;
        } else {
            //로그인 한 유저
            loggedInUserId = principal.getName();
        }

        GetNextCommentListResponse response = commentService.getNextCommentList(request);

        model.addAttribute("loggedInUserId", loggedInUserId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
