package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.friend.friendRequest.RequestFriendListDto;
import com.dbproject.api.member.RegisterFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FriendRequestRepositoryCustomImplTest {


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

        RegisterFormDto registerFormDto3 = new RegisterFormDto();
        registerFormDto3.setName("장원유");
        registerFormDto3.setAddress("대만 산총구");
        registerFormDto3.setEmail("yunni@yunni.com");
        registerFormDto3.setPassword("1234");

        Member member3 = Member.createMember(registerFormDto3, passwordEncoder);
        memberRepository.save(member3);
    }

    @DisplayName("친구 요청 목록 페이지를 가져온다")
    @Test
    void getRequestFriendListPage() {

        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member lee = memberRepository.findByEmail("qwer@qwer.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");

        FriendRequest friendRequest1 = FriendRequest.createFriendRequest(lee, yunni, "1");
        FriendRequest friendRequest2 = FriendRequest.createFriendRequest(lee, son, "1");
        FriendRequest friendRequest3 = FriendRequest.createFriendRequest(yunni, lee, "1");
        FriendRequest friendRequest4 = FriendRequest.createFriendRequest(son, lee, "1");

        friendRequestRepository.save(friendRequest1);
        friendRequestRepository.save(friendRequest2);
        friendRequestRepository.save(friendRequest3);
        friendRequestRepository.save(friendRequest4);

        Pageable pageable = PageRequest.of( 0, 5 );

        //when
        Page<RequestFriendListDto> list = friendRequestRepository.getRequestFriendListPage(pageable, "yunni@yunni.com");

        //then
        assertThat(list.getTotalElements()).isEqualTo(1);
        assertThat(list.getContent().get(0).getEmail()).isEqualTo("qwer@qwer.com");
//        assertThat(list.getContent().get(1).getEmail()).isEqualTo("yunni@yunni.com");
    }
}