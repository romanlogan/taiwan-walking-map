package com.dbproject.service;

import com.dbproject.api.comment.CommentService;
import com.dbproject.api.comment.CreateCommentRequest;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.RegisterFormDto;
import com.dbproject.api.comment.Comment;
import com.dbproject.api.member.Member;
import com.dbproject.api.comment.CommentRepository;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.exception.DuplicateCreateCommentException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        RegisterFormDto registerFormDto2 = new RegisterFormDto();
        registerFormDto2.setName("이병민");
        registerFormDto2.setAddress("강원도 원주시");
        registerFormDto2.setEmail("qwer@qwer.com");
        registerFormDto2.setPassword("1234");

        Member member2 = Member.createMember(registerFormDto2, passwordEncoder);
        memberRepository.save(member2);

        RegisterFormDto registerFormDto3 = new RegisterFormDto();
        registerFormDto3.setName("장원유");
        registerFormDto3.setAddress("대만 산총구");
        registerFormDto3.setEmail("yunni@yunni.com");
        registerFormDto3.setPassword("1234");

        Member member3 = Member.createMember(registerFormDto3, passwordEncoder);
        memberRepository.save(member3);
    }

    @DisplayName("댓글을 저장하지 않았으면 시작은 null")
    @Test
    void CommentCountIsNullBeforeAddComment() {

        //when
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");


        //then
        assertThat(location.getCommentCount()).isNull();
    }


    @DisplayName("createCommentRequest 를 받아서 댓글을 저장합니다.")
    @Test
    void createComment() {

        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        CreateCommentRequest createCommentRequest = new CreateCommentRequest(locationId, content);

        //when
        commentService.createComment(createCommentRequest, email);

        //then
        List<Comment> commentList = commentRepository.findAll();
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        assertThat(location.getCommentCount()).isEqualTo(1);
        assertThat(commentList).hasSize(1);
        assertThat(commentList.get(0).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(commentList.get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(commentList.get(0).getLocation().getName()).isEqualTo("西門町");
    }


    @DisplayName("한 장소에 다른 멤버의 2개의 댓글을 저장합니다.")
    @Test
    void createTwoCommentAtOneLocation() {

        //댓글은 한번밖에 못쓰게 ?
        String email1 = "zxcv@zxcv.com";
        String email2 = "qwer@qwer.com";
        CreateCommentRequest createCommentRequest1 = getCommentRequest();
        CreateCommentRequest createCommentRequest2 = getCommentRequest();

        //when
        commentService.createComment(createCommentRequest1, email1);
        commentService.createComment(createCommentRequest2, email2);

        //then
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        assertThat(location.getCommentCount()).isEqualTo(2);
    }

    private CreateCommentRequest getCommentRequest() {
        int rating = 5;
        String content = "댓글입니다.";
        String locationId = "C1_379000000A_001572";
        return new CreateCommentRequest(locationId, content);
    }
}