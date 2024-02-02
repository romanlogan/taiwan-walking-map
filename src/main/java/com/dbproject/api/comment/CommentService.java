package com.dbproject.api.comment;

import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
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
        location.increaseCommentCount();
        Comment comment = Comment.createComment(createCommentRequest.getContent(), createCommentRequest.getRate(), member, location);

        return commentRepository.save(comment).getId();
    }


}
