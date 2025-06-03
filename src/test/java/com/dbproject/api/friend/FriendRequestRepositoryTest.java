package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.repository.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.api.member.dto.RegisterFormDto;
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
        registerFormDto1.setPhoneNumber("0912345678");
        registerFormDto1.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto1.setGender("1");
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
        registerFormDto2.setGender("1");
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
        registerFormDto3.setGender("1");
        registerFormDto3.setAcceptReceiveAdvertising(true);

        Member member3 = Member.createMember(registerFormDto3, passwordEncoder);
        memberRepository.save(member3);
    }

    @DisplayName("친구 요청을 저장한다")
    @Test
    void saveFriendRequest(){
        //given
//        saveFriendRequest("qwer@qewr.com","zxcv@zxcv.com");

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
        assertThat(friendRequestList.get(0).getFriendRequestStatus()).isEqualTo(FriendRequestStatus.WAITING);
    }

    @DisplayName("요청자와 응답자로 친구 요청을 찾는다")
    @Test
    void findByRequesterAndRespondent(){

        //        테스트용 h2 create 시 constraint key 문제로 안되는건가 ?
        //given
        saveFriendRequest("qwer@qwer.com","zxcv@zxcv.com");

        //when
        FriendRequest savedFriendRequest = friendRequestRepository.findByRequesterEmailAndRespondentEmail("qwer@qwer.com", "zxcv@zxcv.com");

        //then
        assertThat(savedFriendRequest.getRequester().getName()).isEqualTo("이병민");
        assertThat(savedFriendRequest.getRespondent().getName()).isEqualTo("손흥민");
        assertThat(savedFriendRequest.getFriendRequestStatus()).isEqualTo(FriendRequestStatus.WAITING);

    }

    @DisplayName("회원의 email 로 친구 요청에 회원의 email 이 들어가는 요청을 삭제합니다 ")
    @Test
    void deleteByEmail(){
        //given
//        son -> lee
        saveFriendRequest("zxcv@zxcv.com", "qwer@qwer.com");
//        yunni -> lee
        saveFriendRequest("yunni@yunni.com", "qwer@qwer.com");
        List<FriendRequest> savedFriendRequestList = friendRequestRepository.findAll();
        assertThat(savedFriendRequestList).hasSize(2);

        //when
        friendRequestRepository.deleteByEmail("qwer@qwer.com");

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList).hasSize(0);
    }

    private void saveFriendRequest(String requester, String respondent) {
        Member member = memberRepository.findByEmail(requester);
        Member friend = memberRepository.findByEmail(respondent);
        String memo = "memo1";
        FriendRequest friendRequest = FriendRequest.createFriendRequest(member, friend, memo);
        friendRequestRepository.save(friendRequest);
    }


}