package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.RegisterFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FriendRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRepository friendRepository;

    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto1 = new RegisterFormDto();
        registerFormDto1.setName("손흥민");
        registerFormDto1.setAddress("서울 강남구");
        registerFormDto1.setEmail("zxcv@zxcv.com");
        registerFormDto1.setPassword("1234");
        registerFormDto1.setPhoneNumber("0912345678");
        registerFormDto1.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto1.setGender(1);
        registerFormDto1.setAcceptReceiveAdvertising(true);


        Member member1 = Member.createMember(registerFormDto1, passwordEncoder);
        memberRepository.save(member1);

        RegisterFormDto registerFormDto2 = new RegisterFormDto();
        registerFormDto2.setName("이병민");
        registerFormDto2.setAddress("강원도 원주시");
        registerFormDto2.setEmail("qwer@qwer.com");
        registerFormDto2.setPassword("1234");
        registerFormDto2.setPhoneNumber("0912345678");
        registerFormDto2.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto2.setGender(1);
        registerFormDto2.setAcceptReceiveAdvertising(true);

        Member member2 = Member.createMember(registerFormDto2, passwordEncoder);
        memberRepository.save(member2);


        RegisterFormDto registerFormDto3 = new RegisterFormDto();
        registerFormDto3.setName("장원유");
        registerFormDto3.setAddress("대만 산총구");
        registerFormDto3.setEmail("yunni@yunni.com");
        registerFormDto3.setPassword("1234");
        registerFormDto3.setPhoneNumber("0912345678");
        registerFormDto3.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto3.setGender(1);
        registerFormDto3.setAcceptReceiveAdvertising(true);

        Member member3 = Member.createMember(registerFormDto3, passwordEncoder);
        memberRepository.save(member3);

    }

    private void setFriendship() {
        Member lee = memberRepository.findByEmail("qwer@qwer.com");
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");
        //1. 장원유 <-> 이병민 friend 로 등록
        Friend friend1 = new Friend(lee, yunni);
        Friend friend2 = new Friend(yunni, lee);
        friendRepository.save(friend1);
        friendRepository.save(friend2);

        //1. 손흥민 <-> 이병민 friend 로 등록
        Friend friend3 = new Friend(lee, son);
        Friend friend4 = new Friend(son, lee);
        friendRepository.save(friend3);
        friendRepository.save(friend4);
    }

    @DisplayName("회원의 email 로 그 회원의 친구 리스트를 가져옵니다 ")
    @Test
    void getFriendList(){
        //given
        setFriendship();

        //when
        List<Friend> friendList = friendRepository.getFriendList("qwer@qwer.com");

        //then
        assertThat(friendList).hasSize(2);
        assertThat(friendList.get(0).getNewFriend().getName()).isEqualTo("손흥민");
        assertThat(friendList.get(1).getNewFriend().getName()).isEqualTo("장원유");
     }


    @DisplayName("회원의 email 로 그 회원의 친구 리스트와 그 회원을 친구로 둔 친구 관계를 삭제합니다")
    @Test
    void deleteByEmail(){
        //given
        setFriendship();
        List<Friend> savedFriendList = friendRepository.findAll();
        assertThat(savedFriendList).hasSize(4);

        //when
        friendRepository.deleteByEmail("qwer@qwer.com");

        //then
        List<Friend> friendList = friendRepository.findAll();
        assertThat(friendList).hasSize(0);


    }


}