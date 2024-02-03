package com.dbproject.api.comment;


import com.dbproject.api.baseEntity.BaseEntity;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_email")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Builder
    public Comment(String content, Member member, Location location) {
        this.content = content;
        this.member = member;
        this.location = location;
    }

    public Comment() {

    }

    public static Comment createComment(String content, Member member, Location location) {

        return Comment.builder()
                .content(content)
                .member(member)
                .location(location)
                .build();
    }
}
