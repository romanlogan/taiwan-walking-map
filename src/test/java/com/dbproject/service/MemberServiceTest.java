package com.dbproject.service;

import com.dbproject.api.member.MemberService;
import com.dbproject.exception.DuplicateUpdateMemberAddressException;
import com.dbproject.exception.DuplicateUpdateMemberNameException;
import com.dbproject.api.member.RegisterFormDto;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.myPage.UpdateProfileDto;
import javassist.bytecode.DuplicateMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

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


    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("asdf@asdf.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }


    @DisplayName("Member 정보를 받아서 Member 를 생성하고 저장한다 ")
    @Test
    void saveMemberTest() throws DuplicateMemberException {
        //given
        //when
        //then
        Member member = memberRepository.findByEmail("asdf@asdf.com");

        assertThat(member.getName()).isEqualTo("손흥민");
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
        UpdateProfileDto updateProfileDto = new UpdateProfileDto("이병민","강원 원주시");

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
        UpdateProfileDto updateProfileDto = new UpdateProfileDto("이병민","서울 강남구");

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
        UpdateProfileDto updateProfileDto = new UpdateProfileDto("손흥민","강원 원주시");

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


}