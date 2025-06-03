package com.dbproject.api.comment.service;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.*;
import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.exception.CommentNotExistException;
import com.dbproject.exception.DuplicateCreateCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;

    public Long createComment(CreateCommentRequest request, String email) {

        checkDuplicateCreateComment(request,email);
        Comment comment = getCommentFrom(email, request.getContent(), request.getLocationId(), request.getRate());

        return commentRepository.save(comment).getId();
    }


    private Comment getCommentFrom(String email, String content, String locationId, Integer rate) {

        return Comment.create(content,
                memberRepository.findByEmail(email),
                locationRepository.findByLocationId(locationId),
                rate);
    }


    public void checkDuplicateCreateComment(CreateCommentRequest createCommentRequest, String email) {

        Optional<Comment> optionalComment = commentRepository.findDuplicateComment(createCommentRequest.getLocationId(), email);

        if (optionalComment.isPresent()) {
            throw new DuplicateCreateCommentException("A comment has already been posted.");
        }
    }

    public Long updateComment(UpdateCommentRequest request, String email) {

        Comment comment = getCommentFrom(email, request.getLocationId());
        checkCommentExist(comment);
        comment.updateComment(request);

        return comment.getId();
    }

    private Comment getCommentFrom(String email, String locationId) {
        return commentRepository.findByLocationIdAndEmail(locationId, email);
    }

    private static void checkCommentExist(Comment savedComment) {

        if (savedComment == null) {
            throw new CommentNotExistException("This comment does not exist.");
        }
    }

    public void deleteComment(DeleteCommentRequest request) {

        Optional<Comment> comment = getCommentFrom(request.getCommentId());
        delete(comment);

    }

    private void delete(Optional<Comment> optionalComment) {

        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            comment.decreaseCommentCount();
            commentRepository.delete(comment);
        }
    }

    private Optional<Comment> getCommentFrom(Integer commentId) {

        Optional<Comment> comment = commentRepository.findById(Long.valueOf(commentId));

        if (comment.isEmpty()) {
            throw new CommentNotExistException("This comment does not exist.");
        }

        return comment;
    }

//    get next comment page
    public GetNextCommentListResponse getNextCommentList(GetNextCommentListRequest request){

        Pageable pageable = PageRequest.of(request.getPage(), 5);

//        get comment list with member and member_img
        List<CommentDto> commentDtoList = commentRepository.findByLocationIdWithJoin(request.getLocationId(), pageable);

//        if member_img does not exist, set default img (Saving member photos is not mandatory)
        for (CommentDto comment : commentDtoList) {
            if (comment.getImgUrl() == null) {
                comment.setImgUrl("/img/noImg.jpg");
            }
        }

        return new GetNextCommentListResponse(commentDtoList);
    }
}
