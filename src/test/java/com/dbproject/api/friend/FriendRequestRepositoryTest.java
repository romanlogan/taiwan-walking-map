package com.dbproject.api.friend;

import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.web.member.RegisterFormDto;
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
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FriendRequestRepositoryTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto1 = new RegisterFormDto();
        registerFormDto1.setName("손흥민");
        registerFormDto1.setAddress("서울 강남구");
        registerFormDto1.setEmail("zxcv@zxcv.com");
        registerFormDto1.setPassword("1234");

        Member member1 = Member.createMember(registerFormDto1, passwordEncoder);
        memberRepository.save(member1);

        RegisterFormDto registerFormDto2 = new RegisterFormDto();
        registerFormDto2.setName("이병민");
        registerFormDto2.setAddress("강원도 원주시");
        registerFormDto2.setEmail("qwer@qwer.com");
        registerFormDto2.setPassword("1234");

        Member member2 = Member.createMember(registerFormDto2, passwordEncoder);
        memberRepository.save(member2);
    }

    @DisplayName("친구 요청을 저장한다")
    @Test
    void test(){
        //given
        Member member = memberRepository.findByEmail("qwer@qwer.com");
        Member friend = memberRepository.findByEmail("zxcv@zxcv.com");
        String memo = "memo1";
        FriendRequest friendRequest = FriendRequest.createFriendRequest(member, friend, memo);


        //when
        friendRequestRepository.save(friendRequest);

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList.get(0).getRequester().getName()).isEqualTo("이병민");
        assertThat(friendRequestList.get(0).getRespondent().getName()).isEqualTo("손흥민");



    }
}