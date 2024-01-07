package com.dbproject.service;

import com.dbproject.dto.CreateCommentRequest;
import com.dbproject.entity.Comment;
import com.dbproject.entity.Location;
import com.dbproject.entity.Member;
import com.dbproject.repository.CommentRepository;
import com.dbproject.repository.LocationRepository;
import com.dbproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Comment comment = Comment.createComment(createCommentRequest.getContent(), createCommentRequest.getRate(), member, location);

        return commentRepository.save(comment).getId();
    }


}
