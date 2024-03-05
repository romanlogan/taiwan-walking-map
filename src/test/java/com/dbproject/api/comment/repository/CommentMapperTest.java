package com.dbproject.api.comment.repository;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.UserComment;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CommentMapperTest {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


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




    private Comment getComment(int rating, String content) {

        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);

        Comment comment = Comment.createComment(content, member, location,rating);
        return comment;
    }


    @DisplayName("")
    @Test
    void save(){
        //given
        UserComment userComment = new UserComment("memo1", 5, "zxcv@zxcv.com", "C1_379000000A_001572");
        commentMapper.save(userComment);

        //when

        //then
     }

     @DisplayName("")
     @Test
     void test(){
         //given
         Comment comment = commentMapper.findByLocationIdAndEmail("C1_379000000A_001572", "zxcv@zxcv.com");

         //when
        assertThat(comment).isNull();

         //then

      }

}