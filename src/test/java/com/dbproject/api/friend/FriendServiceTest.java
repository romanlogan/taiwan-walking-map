package com.dbproject.api.friend;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.FriendRequestRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.exception.DuplicateFriendRequestException;
import com.dbproject.api.friend.friendRequest.RejectFriendRequest;
import com.dbproject.api.member.RegisterFormDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FriendServiceTest {


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

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

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


    @DisplayName("요청자와 응답자의 이메일을 받아서 요청 정보를 저장한다")
    @Test
    void saveFriendRequest(){
        //given
        String respondentEmail = "zxcv@zxcv.com";
        String requesterEmail = "qwer@qwer.com";
        AddFriendRequest addFriendRequest = new AddFriendRequest(respondentEmail, "memo1");

        //when
        friendService.saveFriendRequest(addFriendRequest, requesterEmail);

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList.get(0).getRequester().getName()).isEqualTo("이병민");
        assertThat(friendRequestList.get(0).getRespondent().getName()).isEqualTo("손흥민");
        assertThat(friendRequestList.get(0).getFriendRequestStatus()).isEqualTo(FriendRequestStatus.WAITING);
    }

    @DisplayName("중복된 친구 요청시 DuplicateFriendRequest 에러를 발생시킨다")
    @Test
    void saveFriendRequestWithDuplicateFriendRequest(){
        //given
        String respondentEmail = "zxcv@zxcv.com";
        String requesterEmail = "qwer@qwer.com";
        AddFriendRequest addFriendRequest = new AddFriendRequest(respondentEmail, "memo1");
        friendService.saveFriendRequest(addFriendRequest, requesterEmail);

        //when
        //then
        assertThatThrownBy(() -> friendService.saveFriendRequest(addFriendRequest, requesterEmail))
                .isInstanceOf(DuplicateFriendRequestException.class)
                .hasMessage("이미 요청한 사용자 입니다.");
    }

    @DisplayName("친구 요청 id 로 친구를 양방향으로 생성하고 친구요청을 수락됨으로 변경한다")
    @Test
    void acceptAddFriend() {

        //given
//        lee -> son
        String requesterEmail = "qwer@qwer.com";
        Member requester = memberRepository.findByEmail(requesterEmail);
        String respondentEmail = "zxcv@zxcv.com";
        Member respondent = memberRepository.findByEmail(respondentEmail);

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, "memo1");
        Long friendRequestId = friendRequestRepository.save(friendRequest).getId();
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(friendRequestId);

        //when
        friendService.acceptAddFriend(acceptAddFriendRequest);

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        List<Friend> friendList = friendRepository.findAll();

        assertThat(friendRequestList).hasSize(1);
        assertThat(friendRequestList.get(0).getFriendRequestStatus()).isEqualTo(FriendRequestStatus.ACCEPTED);
        assertThat(friendList).hasSize(2);
        assertThat(friendList.get(0).getMe().getEmail()).isEqualTo("qwer@qwer.com");
        assertThat(friendList.get(0).getNewFriend().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(friendList.get(1).getMe().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(friendList.get(1).getNewFriend().getEmail()).isEqualTo("qwer@qwer.com");
    }


    @DisplayName("친구 요청 id 로 친구요청을 거절상태로 변경한다")
    @Test
    void deleteFriendRequest(){
        //given
        String requesterEmail = "qwer@qwer.com";
        Member requester = memberRepository.findByEmail(requesterEmail);
        String respondentEmail = "zxcv@zxcv.com";
        Member respondent = memberRepository.findByEmail(respondentEmail);

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, "memo1");
        Long friendRequestId = friendRequestRepository.save(friendRequest).getId();
        RejectFriendRequest rejectFriendRequest = new RejectFriendRequest(friendRequestId);

        //when
        friendService.rejectFriendRequest(rejectFriendRequest);

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList.get(0).getFriendRequestStatus()).isEqualTo(FriendRequestStatus.REJECTED);
     }

//     @DisplayName("친구 목록 페이징을 가져올때 즐겨찾기 리스트도 같이 가져옵니다")
//     @Test
//     void getFriendListWithFavoriteList(){
//
//         //given
//         Member son = memberRepository.findByEmail("zxcv@zxcv.com");
//         Member lee = memberRepository.findByEmail("qwer@qwer.com");
//         Member yunni = memberRepository.findByEmail("yunni@yunni.com");
//         createRequestAndAccept(lee, son);
//         createRequestAndAccept(lee, yunni);
//         Pageable pageable = PageRequest.of( 0, 5 );
//         saveFavoriteLocation(lee);
//
//         //when
//         FriendListResponse friendListResponse = friendService.getFriendList(pageable, "qwer@qwer.com");
//
//         //then
//         assertThat(friendListResponse.getFriendListPages().getTotalElements()).isEqualTo(2);
//         assertThat(friendListResponse.getFriendListPages().getContent().get(0).getFriendName()).isEqualTo("손흥민");
//         assertThat(friendListResponse.getFriendListPages().getContent().get(0).getFriendEmail()).isEqualTo("zxcv@zxcv.com");
//         assertThat(friendListResponse.getFriendListPages().getContent().get(0).getFriendAddress()).isEqualTo("서울 강남구");
//         assertThat(friendListResponse.getFriendListPages().getContent().get(1).getFriendName()).isEqualTo("장원유");
//         assertThat(friendListResponse.getFriendListPages().getContent().get(1).getFriendEmail()).isEqualTo("yunni@yunni.com");
//         assertThat(friendListResponse.getFriendListPages().getContent().get(1).getFriendAddress()).isEqualTo("대만 산총구");
//         assertThat(friendListResponse.getFavoriteLocationList().get(0).getName()).isEqualTo("西門町");
//         assertThat(friendListResponse.getFavoriteLocationList().get(0).getLocationId()).isEqualTo("C1_379000000A_001572");
//
//
//     }

    private void saveFavoriteLocation(Member member) {
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모 1 입니다.";
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);
    }

    private void createRequestAndAccept(Member requester , Member respondent) {
        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, "1");
        Long id = friendRequestRepository.save(friendRequest).getId();
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(id);
        friendService.acceptAddFriend(acceptAddFriendRequest);
    }


}