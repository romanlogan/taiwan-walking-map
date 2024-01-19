package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
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

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FriendRepositoryCustomImplTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendService friendService;

    @Autowired
    private FriendRepository friendRepository;



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


    @DisplayName("친구 목록을 페이징하여 가져온다")
    @Test
    void getFriendListPage() {

        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member lee = memberRepository.findByEmail("qwer@qwer.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");

        createRequestAndAccept(lee, son);
        createRequestAndAccept(lee, yunni);


        Pageable pageable = PageRequest.of( 0, 5 );

        //when
        Page<FriendDto> friendListResponsePage = friendRepository.getFriendListPage(pageable, "qwer@qwer.com");

        //then
        assertThat(friendListResponsePage.getTotalElements()).isEqualTo(2);
        assertThat(friendListResponsePage.getContent().get(0).getFriendName()).isEqualTo("손흥민");
        assertThat(friendListResponsePage.getContent().get(0).getFriendEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(friendListResponsePage.getContent().get(0).getFriendAddress()).isEqualTo("서울 강남구");
        assertThat(friendListResponsePage.getContent().get(1).getFriendName()).isEqualTo("장원유");
        assertThat(friendListResponsePage.getContent().get(1).getFriendEmail()).isEqualTo("yunni@yunni.com");
        assertThat(friendListResponsePage.getContent().get(1).getFriendAddress()).isEqualTo("대만 산총구");



    }

    private void createRequestAndAccept(Member requester , Member respondent) {
        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, "1");
        Long id = friendRequestRepository.save(friendRequest).getId();
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(id);
        friendService.acceptAddFriend(acceptAddFriendRequest);
    }


}