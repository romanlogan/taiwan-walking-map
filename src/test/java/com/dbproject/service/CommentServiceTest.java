package com.dbproject.service;

import com.dbproject.api.comment.CommentService;
import com.dbproject.web.comment.CreateCommentRequest;
import com.dbproject.web.member.RegisterFormDto;
import com.dbproject.api.comment.Comment;
import com.dbproject.api.member.Member;
import com.dbproject.api.comment.CommentRepository;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CommentServiceTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("zxcv@zxcv.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }

    @DisplayName("createCommentRequest 를 받아서 댓글을 저장합니다.")
    @Test
    void createComment() {

        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content, rating);

        //when
        commentService.createComment(createCommentRequest, email);

        //then
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(1);
        assertThat(commentList.get(0).getRate()).isEqualTo(5);
        assertThat(commentList.get(0).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(commentList.get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(commentList.get(0).getLocation().getName()).isEqualTo("西門町");

    }
}