package com.dbproject.repository;

import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

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




    @DisplayName("이메일을 받아서 회원을 삭제합니다")
    @Test
    void deleteByEmail(){

        Member member = memberRepository.findByEmail("asdf@asdf.com");
        List<Member> memberList = memberRepository.findAll();

        assertThat(member.getName()).isEqualTo("손흥민");
        assertThat(memberList).hasSize(1);

        memberRepository.deleteByEmail("asdf@asdf.com");
        List<Member> afterMemberList = memberRepository.findAll();
        assertThat(afterMemberList).hasSize(0);

     }

}