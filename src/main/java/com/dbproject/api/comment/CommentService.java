package com.dbproject.api.comment;

import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.exception.DuplicateCreateCommentException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

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
}
