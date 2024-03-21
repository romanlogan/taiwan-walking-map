package com.dbproject.web.comment;

import com.dbproject.api.ApiResponse;
import com.dbproject.api.comment.service.CommentService;
import com.dbproject.api.comment.dto.CreateCommentRequest;
import com.dbproject.api.comment.dto.DeleteCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import com.dbproject.binding.CheckBindingResult;
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

//        모달창으로 넘어온 값은 모델로 다시 안보내줘도 될것같은데 ?

//        개인적으로 처리
//        오류가 2개 이상일때 append 로 붙이면 어떻게 검증하지 ?
//        if (bindingResult.hasErrors()) {
//
//            List<String> messageList = new ArrayList<>();
//            List<Object> dataList = new ArrayList<>();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                dataList.add(fieldError.getRejectedValue());
//                messageList.add(fieldError.getDefaultMessage());
//            }
//
//
//            return new ResponseEntity(ApiResponse.of(
//                    HttpStatus.BAD_REQUEST,
//                    messageList,
//                    dataList
//            ),HttpStatus.OK);
//        }

        ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
        if (responseEntity != null) {
            return responseEntity;
        }

        commentService.checkDuplicateCreateComment(createCommentRequest,principal.getName());
        Long commentId = commentService.createComment(createCommentRequest, principal.getName());

        return new ResponseEntity(ApiResponse.of(
                HttpStatus.OK,
                null,
                List.of(commentId)
        ), HttpStatus.OK);
    }

    @PutMapping("/updateComment")
    public ResponseEntity updateComment(@Valid @RequestBody UpdateCommentRequest updateCommentRequest,
                                        BindingResult bindingResult,
                                        Principal principal) {

        // 댓글 수정시 request 에 binding error 가 있으면 다시 data 를 반환하여 문제가 있는 data 를 사용자가 볼수있
//        if (bindingResult.hasErrors()) {
//
//            List<String> messageList = new ArrayList<>();
//            List<Object> dataList = new ArrayList<>();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                dataList.add(fieldError.getRejectedValue());
//                messageList.add(fieldError.getDefaultMessage());
//            }
//
//
//            return new ResponseEntity(ApiResponse.of(
//                    HttpStatus.BAD_REQUEST,
//                    messageList,
//                    dataList
//            ),HttpStatus.OK);
//        }
        ResponseEntity responseEntity = CheckBindingResult.induceSuccessInAjax(bindingResult);
        if (responseEntity != null) {
            return responseEntity;
        }


        commentService.updateComment(updateCommentRequest, principal.getName());

        return new ResponseEntity(1L, HttpStatus.OK);
    }

    @DeleteMapping("/deleteComment")
    public ResponseEntity deleteComment(@Valid @RequestBody DeleteCommentRequest deleteCommentRequest,
                                        BindingResult bindingResult,
                                        Principal principal) {

//        if (bindingResult.hasErrors()) {
//
//            List<String> messageList = new ArrayList<>();
//            List<Object> dataList = new ArrayList<>();
//            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
//            for (FieldError fieldError : fieldErrors) {
//                dataList.add(fieldError.getRejectedValue());
//                messageList.add(fieldError.getDefaultMessage());
//            }
//
//
//            return new ResponseEntity(ApiResponse.of(
//                    HttpStatus.BAD_REQUEST,
//                    messageList,
//                    dataList
//            ),HttpStatus.BAD_REQUEST);
//        }


        commentService.deleteComment(deleteCommentRequest.getCommentId());

        return new ResponseEntity(1L, HttpStatus.OK);
    }
}
