package com.dbproject.api.comment;

import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class CommentJpaRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

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

    @DisplayName("댓글을 저장합니다")
    @Test
    void save(){
        //given
        Comment comment = getComment(5,"댓글1 입니다.");

        //when
        commentRepository.save(comment);

        //then
        List<Comment> commentList = commentRepository.findAll();
        assertThat(commentList).hasSize(1);
        assertThat(commentList.get(0).getLocation().getName()).isEqualTo("西門町");
    }


    @DisplayName("댓글 저장시 이미 같은 장소에 같은 아이디로 댓글이 있으면 null 이 아닌 Optional 객체를 반환합니다")
    @Test
    void findDuplicateCommentWhenSaved(){
        //given
        Comment comment = getComment(5,"댓글1 입니다.");
        commentRepository.save(comment);

        //when
        Optional<Comment> optionalComment = commentRepository.findDuplicateComment("C1_379000000A_001572", "zxcv@zxcv.com");

        // then
        assertThat(optionalComment.isPresent()).isTrue();
    }

    @DisplayName("댓글 저장시 이미 같은 장소에 같은 아이디로 댓글이 없으면 null 인 Optional 객체를 반환합니다")
    @Test
    void findDuplicateCommentWhenNotSaved(){
        //given

        //when
        Optional<Comment> optionalComment = commentRepository.findDuplicateComment("C1_379000000A_001572", "zxcv@zxcv.com");

        // then
        assertThat(optionalComment.isEmpty()).isTrue();
    }

    private Comment getComment(int rating, String content) {

        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);

        Comment comment = Comment.create(content, member, location,rating);
        return comment;
    }

    @DisplayName("댓글을 저장후 댓글을 수정하기위해 다시 가져옵니다")
    @Test
    void test(){
        //given
        Comment comment1 = getComment(5,"댓글1 입니다.");
        commentRepository.save(comment1);

        //when
        Comment savedComment = commentRepository.findByLocationIdAndEmail("C1_379000000A_001572", "zxcv@zxcv.com");

        //then
        assertThat(savedComment.getContent()).isEqualTo("댓글1 입니다.");
        assertThat(savedComment.getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(savedComment.getRate()).isEqualTo(5);
     }


    @DisplayName("회원이 작성한 댓글 리스트를 가져옵니다")
    @Test
    void findCommentListByMemberEmail(){
        //given
        Comment comment1 = getComment(5,"댓글1 입니다.");
        Comment comment2 = getComment(4,"댓글2 입니다.");
        Comment comment3 = getComment(3,"댓글3 입니다.");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //when
        List<Comment> commentList = commentRepository.findByMemberEmail("zxcv@zxcv.com");

        //then
        assertThat(commentList.get(0).getContent()).isEqualTo("댓글3 입니다.");
        assertThat(commentList.get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(commentList.get(0).getRate()).isEqualTo(3);
        assertThat(commentList.get(1).getContent()).isEqualTo("댓글2 입니다.");
        assertThat(commentList.get(1).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(commentList.get(1).getRate()).isEqualTo(4);
        assertThat(commentList.get(2).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(commentList.get(2).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(commentList.get(2).getRate()).isEqualTo(5);
    }



    @DisplayName("회원이 작성한 댓글 리스트를 삭제합니다")
    @Test
    void deleteCommentListByMemberEmail(){
        //given
        Comment comment1 = getComment(5,"댓글1 입니다.");
        Comment comment2 = getComment(4,"댓글2 입니다.");
        Comment comment3 = getComment(3,"댓글3 입니다.");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //when
        commentRepository.deleteByMemberEmail("zxcv@zxcv.com");

        //then
        List<Comment> commentList = commentRepository.findByMemberEmail("zxcv@zxcv.com");
        assertThat(commentList).hasSize(0);
    }




}