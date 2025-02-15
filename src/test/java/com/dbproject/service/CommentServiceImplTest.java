package com.dbproject.service;

import com.dbproject.api.comment.dto.*;
import com.dbproject.api.comment.service.CommentServiceImpl;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.comment.Comment;
import com.dbproject.api.member.Member;
import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.location.repository.LocationRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CommentServiceImplTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @BeforeEach
    void createMember() {

        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("이병민", "강원도 원주시", "asdf@asdf.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("손흥민", "서울 강남구", "zxcv@zxcv.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("장원유", "대만 산총구", "yunni@yunni.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("lee", "대만 산총구", "lee@lee.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("zhang", "대만 산총구", "qwer@qwer.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("son", "대만 산총구", "son@son.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("kim", "대만 산총구", "kim@kim.com", "1234"), passwordEncoder));
    }



    @DisplayName("total comment count is null, If no user has commented")
    @Test
    void CommentCountIsNullBeforeAddComment() {

        //when
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");

        //then
        assertThat(location.getCommentCount()).isNull();
    }


    @DisplayName("save comment from createCommentRequest.")
    @Test
    void createComment() {

        //given
        CreateCommentRequest request = CreateCommentRequest.of("C1_379000000A_001572", "댓글입니다.", 5);

        //when
        Long id = commentService.createComment(request, "asdf@asdf.com");

        //then
        Comment comment = commentRepository.findById(id).get();
        assertThat(comment.getContent()).isEqualTo("댓글입니다.");
        assertThat(comment.getRate()).isEqualTo(5);
        assertThat(comment.getMember().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(comment.getLocation().getName()).isEqualTo("西門町");
    }

    @DisplayName("location's comment count is 1 after save one comment")
    @Test
    void commentCountIsOneAfterCreateComment() {

        //given
        CreateCommentRequest request = CreateCommentRequest.of("C1_379000000A_001572", "댓글입니다.", 5);

        //when
        commentService.createComment(request, "asdf@asdf.com");

        //then
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        assertThat(location.getCommentCount()).isEqualTo(1);
    }


    @DisplayName("save two comments in one location from different members")
    @Test
    void createTwoCommentAtOneLocation() {

        //given
        CreateCommentRequest request1 = CreateCommentRequest.of("C1_379000000A_001572", "son's comment", 5);
        CreateCommentRequest request2 = CreateCommentRequest.of("C1_379000000A_001572", "lee's comment", 3);

        //when
        commentService.createComment(request1, "zxcv@zxcv.com");
        commentService.createComment(request2, "asdf@asdf.com");

        //then
        List<Comment> comments = commentRepository.findByLocationId("C1_379000000A_001572");
        assertThat(comments.get(0).getContent()).isEqualTo("lee's comment");
        assertThat(comments.get(1).getContent()).isEqualTo("son's comment");
    }

    @DisplayName("comment count is 2 ,after save two comment in one location from different members")
    @Test
    void commentCountIsTwoAfterCreateTwoCommentAtOneLocation() {

        //given
        CreateCommentRequest request1 = CreateCommentRequest.of("C1_379000000A_001572", "son's comment", 5);
        CreateCommentRequest request2 = CreateCommentRequest.of("C1_379000000A_001572", "lee's comment", 3);

        //when
        commentService.createComment(request1, "zxcv@zxcv.com");
        commentService.createComment(request2, "asdf@asdf.com");

        //then
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        assertThat(location.getCommentCount()).isEqualTo(2);
    }


    @DisplayName("throw DuplicateCreateCommentException, when save two comments in one location from same member")
    @Test
    void createTwoCommentAtOneLocationSameMember() {

        //given
        CreateCommentRequest request1 = CreateCommentRequest.of("C1_379000000A_001572", "lee's comment", 5);
        CreateCommentRequest request2 = CreateCommentRequest.of("C1_379000000A_001572", "lee's comment", 3);
        commentService.createComment(request1, "asdf@asdf.com");

        //when
        //then
        assertThatThrownBy(() -> commentService.createComment(request2, "asdf@asdf.com"))
                .isInstanceOf(com.dbproject.exception.DuplicateCreateCommentException.class)
                .hasMessage("이미 댓글을 생성했습니다.");
    }

    @DisplayName("update comment")
    @Test
    void updateComment(){

        //given
        UpdateCommentRequest request = UpdateCommentRequest.create("C1_379000000A_001572", "불만족입니다", 1);

        commentRepository.save(Comment.create("만족합니다",
                memberRepository.findByEmail("asdf@asdf.com"),
                locationRepository.findByLocationId("C1_379000000A_001572"),
                5));

        //when
        Long id = commentService.updateComment(request, "asdf@asdf.com");

        //then
        Comment comment = commentRepository.findById(id).get();
        assertThat(comment.getContent()).isEqualTo("불만족입니다");
        assertThat(comment.getRate()).isEqualTo(1);
    }


    @DisplayName("throw CommentNotExistException, when try update to not exist comment")
    @Test
    void updateCommentWithNotExistComment(){

        //given
        UpdateCommentRequest request = UpdateCommentRequest.create("C1_379000000A_001572", "불만족입니다", 1);

        //when
        //then
        assertThatThrownBy(() -> commentService.updateComment(request, "asdf@asdf.com"))
                .isInstanceOf(com.dbproject.exception.CommentNotExistException.class)
                .hasMessage("댓글이 존재하지 않습니다.");
    }

    @DisplayName("delete comment")
    @Test
    void deleteComment(){

        //given
        Comment savedComment = commentRepository.save(Comment.create("만족합니다",
                memberRepository.findByEmail("asdf@asdf.com"),
                locationRepository.findByLocationId("C1_379000000A_001572"),
                5));

        DeleteCommentRequest request = DeleteCommentRequest.create(Math.toIntExact(savedComment.getId()));
        checkSavedCommentsSize(1);

        //when
        commentService.deleteComment(request);

        //then
        checkSavedCommentsSize(0);
    }

    private void checkSavedCommentsSize(int expected) {
        List<Comment> comments = commentRepository.findAll();
        assertThat(comments.size()).isEqualTo(expected);
    }

    @DisplayName("decrease location's comment count when delete comment")
    @Test
    void decreaseCommentCountWhenDeleteComment(){

        //given
        Comment savedComment = commentRepository.save(Comment.create("만족합니다",
                memberRepository.findByEmail("asdf@asdf.com"),
                locationRepository.findByLocationId("C1_379000000A_001572"),
                5));

        commentRepository.save(Comment.create("만족합니다", memberRepository.findByEmail("asdf@asdf.com"), locationRepository.findByLocationId("C1_379000000A_001572"), 5));
        commentRepository.save(Comment.create("만족합니다", memberRepository.findByEmail("asdf@asdf.com"), locationRepository.findByLocationId("C1_379000000A_001572"), 5));

        DeleteCommentRequest request = DeleteCommentRequest.create(Math.toIntExact(savedComment.getId()));
        checkSavedCommentsSize(3);

        //when
        commentService.deleteComment(request);

        //then
        checkSavedCommentsSize(2);
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        assertThat(location.getCommentCount()).isEqualTo(2);
    }

    @DisplayName("throw CommentNotExistException, when try delete to not exist comment")
    @Test
    void deleteCommentWithNotExistComment(){

        //given
        DeleteCommentRequest request = DeleteCommentRequest.create(1);

        //when
        //then
        assertThatThrownBy(() -> commentService.deleteComment(request))
                .isInstanceOf(com.dbproject.exception.CommentNotExistException.class)
                .hasMessage("댓글이 존재하지 않습니다.");
    }


    @DisplayName("get second page from getNextCommentList")
    @Test
    void getNextCommentList() {

        //given
        saveCommentForPaging();
        GetNextCommentListRequest request = new GetNextCommentListRequest("C1_379000000A_001572", 1);

        //when
        GetNextCommentListResponse response = commentService.getNextCommentList(request);

        //then
        assertThat(response.getCommentDtoList()).hasSize(2);
    }

    private void saveCommentForPaging() {

        Location location = locationRepository.findByLocationId("C1_379000000A_001572");

        List<Member> members = memberRepository.findAll();

        for (Member member : members) {
            commentRepository.save(Comment.create("good",
                    member,
                    location,
                    5));
        }
    }
}