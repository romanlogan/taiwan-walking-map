package com.dbproject.service;

import com.dbproject.dto.RegisterFormDto;
import com.dbproject.entity.Member;
import com.dbproject.repository.MemberRepository;
import javassist.bytecode.DuplicateMemberException;
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


    @DisplayName("Member 정보를 받아서 Member 를 생성하고 저장한다 ")
    @Test
    void saveMemberTest() throws DuplicateMemberException {
        //given
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("asdf@asdf.com");
        registerFormDto.setPassword("1234");

        //when
        memberService.saveMember(registerFormDto, passwordEncoder);

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
        memberService.saveMember(registerFormDto, passwordEncoder);

        //when & then
        assertThatThrownBy(() -> memberService.saveMember(registerFormDto, passwordEncoder))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 아이디가 존재 합니다.");

        //사용자 정의 에러를 테스트 하는 법은 ?
    }



    @DisplayName("email 을 받아서 회원을 삭제한다")
    @Test
    void deleteMemberTest(){
        //given
        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("asdf@asdf.com");
        registerFormDto.setPassword("1234");

        memberService.saveMember(registerFormDto, passwordEncoder);

        //when
        memberService.deleteMember("asdf@asdf.com");

        //then
        Member member = memberRepository.findByEmail("asdf@asdf.com");
        assertThat(member).isNull();

    }


}