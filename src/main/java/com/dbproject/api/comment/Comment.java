package com.dbproject.api.comment;


import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.comment.dto.UpdateCommentRequest;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private Integer rate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn(name = "member_email")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Builder
    public Comment(String content, Member member, Location location, Integer rate) {
        this.content = content;
        this.member = member;
        this.location = location;
        this.rate = rate;
    }

    public Comment() {

    }

    public static Comment createComment(String content, Member member, Location location, Integer rate) {

        return Comment.builder()
                .content(content)
                .member(member)
                .location(location)
                .rate(rate)
                .build();
    }

    public void updateComment(UpdateCommentRequest updateCommentRequest) {
        this.content = updateCommentRequest.getContent();
        this.rate = updateCommentRequest.getRate();
    }
}
