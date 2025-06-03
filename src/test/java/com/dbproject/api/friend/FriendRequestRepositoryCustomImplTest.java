package com.dbproject.api.friend;

import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.repository.FriendRequestRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.friend.friendRequest.dto.RequestFriendListDto;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.constant.FriendRequestStatus;
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
class FriendRequestRepositoryCustomImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @BeforeEach
    void createMember() {

        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("이병민", "강원도 원주시", "asdf@asdf.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("손흥민", "서울 강남구", "zxcv@zxcv.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("장원유", "대만 산총구", "yunni@yunni.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("test1", "대만 산총구", "test1@test1.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("test2", "대만 산총구", "test2@test2.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("test3", "대만 산총구", "test3@test3.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("test4", "대만 산총구", "test4@test4.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("test5", "대만 산총구", "test5@test5.com", "1234"), passwordEncoder));
    }

    @DisplayName("receive requests from two users, and retrieving received friend request list page, there are two results.")
    @Test
    void getRequestFriendListPage() {

        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member lee = memberRepository.findByEmail("asdf@asdf.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");

        createAndSaveFriendRequest(yunni, lee);
        createAndSaveFriendRequest(son, lee);
        createAndSaveFriendRequest(son, yunni);

        Pageable pageable = PageRequest.of( 0, 5 );

        //when
        Page<RequestFriendListDto> list = friendRequestRepository.getRequestFriendListPage(pageable, "asdf@asdf.com");

        //then
        assertThat(list.getTotalElements()).isEqualTo(2);
    }

    private Long createAndSaveFriendRequest(Member requester, Member respondent) {
        FriendRequest friendRequest1 = FriendRequest.createFriendRequest(requester,respondent,"1");
        return friendRequestRepository.save(friendRequest1).getId();
    }

    @DisplayName("receive requests from two users, accept one request, and retrieving received friend request list page, there are one results.")
    @Test
    void getRequestFriendListPageWithOneAccepted() {

        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member lee = memberRepository.findByEmail("asdf@asdf.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");
        Long id = createAndSaveFriendRequest(yunni, lee);
        createAndSaveFriendRequest(son, lee);

        acceptFriendRequest(id);

        Pageable pageable = PageRequest.of( 0, 5 );

        //when
        Page<RequestFriendListDto> list = friendRequestRepository.getRequestFriendListPage(pageable, "asdf@asdf.com");

        //then
        assertThat(list.getTotalElements()).isEqualTo(1);
    }

    private void acceptFriendRequest(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).get();
        friendRequest.changeStatus(FriendRequestStatus.ACCEPTED);
    }

    @DisplayName("receive requests from two users, reject one request, and retrieving received friend request list page, there are one results.")
    @Test
    void getRequestFriendListPageWithOneRejected() {

        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member lee = memberRepository.findByEmail("asdf@asdf.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");
        Long id = createAndSaveFriendRequest(yunni, lee);
        createAndSaveFriendRequest(son, lee);

        rejectFriendRequest(id);

        Pageable pageable = PageRequest.of( 0, 5 );

        //when
        Page<RequestFriendListDto> list = friendRequestRepository.getRequestFriendListPage(pageable, "asdf@asdf.com");

        //then
        assertThat(list.getTotalElements()).isEqualTo(1);
    }

    private void rejectFriendRequest(Long id) {
        FriendRequest friendRequest = friendRequestRepository.findById(id).get();
        friendRequest.changeStatus(FriendRequestStatus.REJECTED);
    }


    @DisplayName("When retrieving the friend request list page, if there are requests from 7 users and the page size is 5, only 2 request appear on the second page.")
    @Test
    void getRequestFriendListSecondPageWithPageSizeIs5() {

        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member lee = memberRepository.findByEmail("asdf@asdf.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");
        Member test1 = memberRepository.findByEmail("test1@test1.com");
        Member test2 = memberRepository.findByEmail("test2@test2.com");
        Member test3 = memberRepository.findByEmail("test3@test3.com");
        Member test4 = memberRepository.findByEmail("test4@test4.com");
        Member test5 = memberRepository.findByEmail("test5@test5.com");

        createAndSaveFriendRequest(yunni, lee);
        createAndSaveFriendRequest(son, lee);
        createAndSaveFriendRequest(test1, lee);
        createAndSaveFriendRequest(test2, lee);
        createAndSaveFriendRequest(test3, lee);
        createAndSaveFriendRequest(test4, lee);
        createAndSaveFriendRequest(test5, lee);

        Pageable pageable = PageRequest.of( 1, 5 );

        //when
        Page<RequestFriendListDto> list = friendRequestRepository.getRequestFriendListPage(pageable, "asdf@asdf.com");

        //then
        assertThat(list.getTotalElements()).isEqualTo(7);
        assertThat(list.getTotalPages()).isEqualTo(2);
        assertThat(list.getContent().size()).isEqualTo(2);
    }

}
