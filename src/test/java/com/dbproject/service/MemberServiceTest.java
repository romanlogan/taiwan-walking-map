package com.dbproject.service;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberService;
import com.dbproject.exception.DuplicateUpdateMemberAddressException;
import com.dbproject.exception.DuplicateUpdateMemberNameException;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.UpdateProfileRequest;
import javassist.bytecode.DuplicateMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LocationRepository locationRepository;


    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setEmail("asdf@asdf.com");
        registerFormDto.setName("손흥민");
        registerFormDto.setPassword("1234");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setPhoneNumber("0912345678");
        registerFormDto.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto.setGender(1);
        registerFormDto.setAcceptReceiveAdvertising(true);


        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }


    @DisplayName("Member 정보를 받아서 Member 를 생성하고 저장한다 ")
    @Test
    void saveMemberTest() throws DuplicateMemberException {
        //given
        //when
        Member member = memberRepository.findByEmail("asdf@asdf.com");

        //then
        assertThat(member.getName()).isEqualTo("손흥민");
        assertThat(member.getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(member.getAddress()).isEqualTo("서울 강남구");
        assertThat(member.getPhoneNumber()).isEqualTo("0912345678");
        assertThat(member.getDateOfBirth()).isEqualTo(LocalDate.parse("1996-12-10"));
        assertThat(member.getGender()).isEqualTo(1);
        assertThat(member.getAcceptReceiveAdvertising()).isEqualTo(true);


    }

    @DisplayName("중복된 Member 정보를 받아서 Member 를 생성을 시도하면 중복된 회원가입 에러를 발생시킨다")
    @Test
    void trySaveDuplicateMemberTest() throws DuplicateMemberException {
        //given
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("asdf@asdf.com");
        registerFormDto.setPassword("1234");
        registerFormDto.setPhoneNumber("0912345678");
        registerFormDto.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto.setGender(1);
        registerFormDto.setAcceptReceiveAdvertising(true);

        //when & then
        assertThatThrownBy(() -> memberService.saveMember(registerFormDto, passwordEncoder))
                .isInstanceOf(com.dbproject.exception.DuplicateMemberException.class)
                .hasMessage("동일한 아이디가 존재 합니다.");

        //사용자 정의 에러를 테스트 하는 법은 ?
    }

    @DisplayName("회원 정보를 수정 합니다")
    @Test
    void updateMemberInfo() {
        //given
        String email = "asdf@asdf.com";
        UpdateProfileRequest updateProfileDto = new UpdateProfileRequest("이병민","강원 원주시");

        //when
        memberService.updateProfile(email, updateProfileDto);

        //then
        Member savedMember = memberRepository.findByEmail(email);
        assertThat(savedMember.getName()).isEqualTo("이병민");
        assertThat(savedMember.getAddress()).isEqualTo("강원 원주시");
    }

    @DisplayName("회원 정보를 수정시 주소가 같으면 DuplicateUpdateMemberAddressException 를 반환합니다")
    @Test
    void updateMemberInfoWithDuplicateAddress() throws DuplicateUpdateMemberAddressException {
        //given
        String email = "asdf@asdf.com";
        UpdateProfileRequest updateProfileDto = new UpdateProfileRequest("이병민","서울 강남구");

        //when
        //then
        assertThatThrownBy(() -> memberService.updateProfile(email, updateProfileDto))
                .isInstanceOf(DuplicateUpdateMemberAddressException.class)
                .hasMessage("이전 주소와 같습니다.");

    }


    @DisplayName("회원 정보를 수정시 이름이 같으면 DuplicateUpdateMemberNameException 를 반환합니다")
    @Test
    void updateMemberInfoWithDuplicateName() throws DuplicateUpdateMemberAddressException {
        //given
        String email = "asdf@asdf.com";
        UpdateProfileRequest updateProfileDto = new UpdateProfileRequest("손흥민","강원 원주시");

        //when
        //then
        assertThatThrownBy(() -> memberService.updateProfile(email, updateProfileDto))
                .isInstanceOf(DuplicateUpdateMemberNameException.class)
                .hasMessage("이전 이름과 같습니다.");
    }

    @DisplayName("email 을 받아서 회원을 삭제한다")
    @Test
    void deleteMemberTest(){
        //given
        //when
        memberService.deleteMember("asdf@asdf.com");

        //then
        Member member = memberRepository.findByEmail("asdf@asdf.com");
        assertThat(member).isNull();
    }

    private Comment getComment(int rating, String content) {

        Member member = memberRepository.findByEmail("asdf@asdf.com");
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);

        Comment comment = Comment.create(content, member, location,rating);
        return comment;
    }

    @DisplayName("댓글이 존재하는 회원의 email 을 받아서 회원을 삭제한다")
    @Test
    void deleteMemberTestWithoutDeleteComment(){

//        h2 작업할때 contraint 는 빼고 create 쿼리를 짰더니 테스트와 mysql 이 다르게 동작
        //given
        Comment comment1 = getComment(5,"댓글1 입니다.");
        Comment comment2 = getComment(4,"댓글2 입니다.");
        Comment comment3 = getComment(3,"댓글3 입니다.");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);

        //when
        //then
//        assertThatThrownBy(memberService.deleteMember("asdf@asdf.com"))/;

    }


}