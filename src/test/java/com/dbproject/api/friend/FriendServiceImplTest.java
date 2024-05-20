package com.dbproject.api.friend;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.friend.dto.AcceptAddFriendRequest;
import com.dbproject.api.friend.dto.AddFriendRequest;
import com.dbproject.api.friend.dto.FriendListResponse;
import com.dbproject.api.friend.repository.FriendRepository;
import com.dbproject.api.friend.service.FriendServiceImpl;
import com.dbproject.api.friend.friendRequest.FriendRequest;
import com.dbproject.api.friend.friendRequest.repository.FriendRequestRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.constant.FriendRequestStatus;
import com.dbproject.exception.DuplicateFriendRequestException;
import com.dbproject.api.friend.friendRequest.dto.RejectFriendRequest;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.exception.FriendRequestNotExistException;
import com.dbproject.exception.MemberNotExistException;
import com.dbproject.exception.UnknownUserTryGetFriendListException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FriendServiceImplTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private FriendServiceImpl friendService;

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @BeforeEach
    void createMember() {
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("이병민", "강원도 원주시", "asdf@asdf.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("손흥민", "서울 강남구", "zxcv@zxcv.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("장원유", "대만 산총구", "yunni@yunni.com", "1234"), passwordEncoder));
    }


    @DisplayName("Requester sends friend request to respondent")
    @Test
    void saveFriendRequest(){
        //given
        String requesterEmail = "asdf@asdf.com";
        String respondentEmail = "zxcv@zxcv.com";
        AddFriendRequest addFriendRequest = new AddFriendRequest(respondentEmail, "memo1");

        //when
        Long id = friendService.saveFriendRequest(addFriendRequest, requesterEmail);

        //then
        FriendRequest friendRequest = friendRequestRepository.findById(id).get();
        assertThat(friendRequest.getRequester().getName()).isEqualTo("이병민");
        assertThat(friendRequest.getRespondent().getName()).isEqualTo("손흥민");
        assertThat(friendRequest.getFriendRequestStatus()).isEqualTo(FriendRequestStatus.WAITING);
    }

    @DisplayName("if request friend to unknown member , then throw FriendNotExistException")
    @Test
    void saveFriendRequestWithNotExistFriendEmail(){
        //given
        String requesterEmail = "asdf@asdf.com";
        String respondentEmail = "unknown@unknown.com";
        AddFriendRequest addFriendRequest = new AddFriendRequest(respondentEmail, "memo1");

        //when then
        assertThatThrownBy(() -> friendService.saveFriendRequest(addFriendRequest, requesterEmail))
                .isInstanceOf(MemberNotExistException.class)
                .hasMessage("존재하지 않는 유저 입니다.");
    }


    @DisplayName("if duplicate friend requests are made , then throw DuplicateFriendRequest ")
    @Test
    void saveFriendRequestWithDuplicateFriendRequest(){
        //given
        String respondentEmail = "zxcv@zxcv.com";
        String requesterEmail = "asdf@asdf.com";
        AddFriendRequest addFriendRequest = new AddFriendRequest(respondentEmail, "memo1");
        friendService.saveFriendRequest(addFriendRequest, requesterEmail);

        //when //then
        assertThatThrownBy(() -> friendService.saveFriendRequest(addFriendRequest, requesterEmail))
                .isInstanceOf(DuplicateFriendRequestException.class)
                .hasMessage("이미 친구 요청한 사용자 입니다.");
    }

    @DisplayName("친구 요청 id 로 친구를 양방향으로 생성하고 친구요청을 수락됨으로 변경한다")
    @Test
    void acceptAddFriend() {

        //given
//        lee -> son
        Long friendRequestId = createAndSaveFriendRequest();
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(friendRequestId);

        //when
        friendService.acceptAddFriend(acceptAddFriendRequest);

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        List<Friend> friendList = friendRepository.findAll();

        assertThat(friendRequestList).hasSize(1);
        assertThat(friendRequestList.get(0).getFriendRequestStatus()).isEqualTo(FriendRequestStatus.ACCEPTED);
        assertThat(friendList).hasSize(2);
        assertThat(friendList.get(0).getMe().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(friendList.get(0).getNewFriend().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(friendList.get(1).getMe().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(friendList.get(1).getNewFriend().getEmail()).isEqualTo("asdf@asdf.com");
    }

    @DisplayName("if accept friend request that does not exist , then throw FriendRequestNotExistException ")
    @Test
    void acceptFriendRequestThatDoesNotExist(){
        //given
        AcceptAddFriendRequest acceptAddFriendRequest = new AcceptAddFriendRequest(1L);

        //when //then
        assertThatThrownBy(() -> friendService.acceptAddFriend(acceptAddFriendRequest))
                .isInstanceOf(FriendRequestNotExistException.class)
                .hasMessage("존재하지 않는 친구 요청 입니다.");
    }


    @DisplayName("친구 요청 id 로 친구요청을 거절상태로 변경한다")
    @Test
    void rejectFriendRequest(){
        //given
        Long friendRequestId = createAndSaveFriendRequest();
        RejectFriendRequest rejectFriendRequest = new RejectFriendRequest(friendRequestId);

        //when
        friendService.rejectFriendRequest(rejectFriendRequest);

        //then
        List<FriendRequest> friendRequestList = friendRequestRepository.findAll();
        assertThat(friendRequestList.get(0).getFriendRequestStatus()).isEqualTo(FriendRequestStatus.REJECTED);
     }

    private Long createAndSaveFriendRequest() {
        Member requester = memberRepository.findByEmail("asdf@asdf.com");
        Member respondent = memberRepository.findByEmail("zxcv@zxcv.com");

        FriendRequest friendRequest = FriendRequest.createFriendRequest(requester, respondent, "memo1");
        return friendRequestRepository.save(friendRequest).getId();
    }

    @DisplayName("if reject friend request that does not exist , then throw FriendRequestNotExistException ")
    @Test
    void rejectFriendRequestThatDoesNotExist(){
        //given
        RejectFriendRequest rejectFriendRequest = new RejectFriendRequest(1L);

        //when //then
        assertThatThrownBy(() -> friendService.rejectFriendRequest(rejectFriendRequest))
                .isInstanceOf(FriendRequestNotExistException.class)
                .hasMessage("존재하지 않는 친구 요청 입니다.");
    }

     @DisplayName("친구 목록 리스트를 가져올때 즐겨찾기 리스트도 같이 가져옵니다")
     @Test
     void getFriendListWithFavoriteList(){

         //given
         Member son = memberRepository.findByEmail("zxcv@zxcv.com");
         Member lee = memberRepository.findByEmail("asdf@asdf.com");
         Member yunni = memberRepository.findByEmail("yunni@yunni.com");
         createRequestAndAccept(lee, son);
         createRequestAndAccept(lee, yunni);
         saveFavoriteLocation(lee);

         //when
         FriendListResponse friendListResponse = friendService.getFriendList("asdf@asdf.com");

         //then
         assertThat(friendListResponse.getFriendDtoList().size()).isEqualTo(2);
         assertThat(friendListResponse.getFriendDtoList().get(0).getFriendName()).isEqualTo("손흥민");
         assertThat(friendListResponse.getFriendDtoList().get(0).getFriendEmail()).isEqualTo("zxcv@zxcv.com");
         assertThat(friendListResponse.getFriendDtoList().get(0).getFriendAddress()).isEqualTo("서울 강남구");
         assertThat(friendListResponse.getFriendDtoList().get(1).getFriendName()).isEqualTo("장원유");
         assertThat(friendListResponse.getFriendDtoList().get(1).getFriendEmail()).isEqualTo("yunni@yunni.com");
         assertThat(friendListResponse.getFriendDtoList().get(1).getFriendAddress()).isEqualTo("대만 산총구");
         assertThat(friendListResponse.getFavoriteLocationList().get(0).getName()).isEqualTo("西門町");
         assertThat(friendListResponse.getFavoriteLocationList().get(0).getLocationId()).isEqualTo("C1_379000000A_001572");
     }

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

    //member without friend -> empty list
    @DisplayName("When get a list of friends, member without any friends will get an empty friend list.")
    @Test
    void getEmptyFriendListWhenMemberWithoutFriend(){

        //given
        Member lee = memberRepository.findByEmail("asdf@asdf.com");

        //when
        FriendListResponse friendListResponse = friendService.getFriendList("asdf@asdf.com");

        //then
        assertThat(friendListResponse.getFriendDtoList().size()).isEqualTo(0);
    }

    //unknown user -> null
    @DisplayName("throw UnknownUserTryGetFriendListException, when unknown user try to get friend list")
    @Test
    void friendListDoesNotExist(){

        //given
        //when //then
        assertThatThrownBy(() -> friendService.getFriendList("unknown@unknown.com"))
                .isInstanceOf(UnknownUserTryGetFriendListException.class)
                .hasMessage("존재하지 않는 유저는 친구 목록을 가져올 수 없습니다.");
    }

    @DisplayName("if member didn't add any favorite location, then get a empty list of favorite location")
    @Test
    void getEmptyFavoriteLocationListWhenMemberNoAdded(){

        //given
        Member lee = memberRepository.findByEmail("asdf@asdf.com");

        //when
        FriendListResponse friendListResponse = friendService.getFriendList("asdf@asdf.com");

        //then
        assertThat(friendListResponse.getFavoriteLocationList().size()).isEqualTo(0);
    }

//    FavoriteLocation 과 Friend 를 가져오는 코드를 분리해야하나 ?
//    @DisplayName("throw UnknownMemberTryGetFavoriteLocationException, when unknown member try to get favorite location list")
//    @Test
//    void favoriteLocationDoesNotExist(){
//
//        //given
//        //when //then
//        assertThatThrownBy(() -> friendService.getFriendList("unknown@unknown.com"))
//                .isInstanceOf(UnknownUserTryGetFriendListException.class)
//                .hasMessage("존재하지 않는 유저는 친구 목록을 가져올 수 없습니다.");
//    }



}