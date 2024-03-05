package com.dbproject.api.comment.service;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.CreateCommentRequest;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.exception.DuplicateCreateCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;



    public Long createComment(CreateCommentRequest createCommentRequest, String email) {

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(createCommentRequest.getLocationId());
        location.increaseCommentCount();
        Comment comment = Comment.createComment(createCommentRequest.getContent(), member, location, createCommentRequest.getRate());

        return commentRepository.save(comment).getId();
    }


    // 아니면 댓글 작성과 평가 작업을 따로 분리 하게 되어 사용 안함
    public void checkDuplicateCreateComment(CreateCommentRequest createCommentRequest, String email) {


        Optional<Comment> optionalComment = commentRepository.findDuplicateComment(createCommentRequest.getLocationId(), email);

        if (optionalComment.isPresent()) {
            throw new DuplicateCreateCommentException("이미 댓글을 생성했습니다");
        }
    }

    public void updateComment(UpdateCommentRequest updateCommentRequest, String email) {

        Comment savedComment = commentRepository.findByLocationIdAndEmail(updateCommentRequest.getLocationId(), email);
        savedComment.updateComment(updateCommentRequest);
    }

    public void deleteComment(Integer commentId) {

        Optional<Comment> optionalComment = commentRepository.findById(Long.valueOf(commentId));
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            commentRepository.delete(comment);
        }

    }
}
