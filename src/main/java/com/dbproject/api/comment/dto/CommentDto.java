package com.dbproject.api.comment.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class CommentDto {

    private Long id;
    private String content;
    private String email;
    private String name;
    private LocalDateTime regTime;
    private String imgUrl;

    public CommentDto() {
    }

    @Builder
    public CommentDto(Long id, String content, String email, String name, LocalDateTime regTime, String imgUrl) {
        this.id = id;
        this.content = content;
        this.email = email;
        this.name = name;
        this.regTime = regTime;
        this.imgUrl = imgUrl;
    }


//    public static CommentDto from(CommentDto comment, String imgUrl) {
//
//        return CommentDto.builder()
//                .id(comment.getId())
//                .content(comment.getContent())
//                .email(comment.getMember().getEmail())
//                .name(comment.getMember().getName())
//                .regTime(comment.getRegTime())
//                .imgUrl(imgUrl)
//                .build();
//    }

}
