package com.dbproject.entity;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Getter
@Setter
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String content;

    private Integer rate;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    @Builder
    public Comment(String content, Integer rate, Member member, Location location) {
        this.content = content;
        this.rate = rate;
        this.member = member;
        this.location = location;
    }

    public Comment() {

    }

    public static Comment createComment(String content, Integer rate, Member member, Location location) {

        return Comment.builder()
                .content(content)
                .rate(rate)
                .member(member)
                .location(location)
                .build();
    }
}
