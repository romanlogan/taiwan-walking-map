package com.dbproject.service;

import com.dbproject.api.friend.Friend;
import com.dbproject.api.friend.repository.FriendRepository;
import com.dbproject.api.location.service.LocationServiceImpl;
import com.dbproject.api.location.dto.LocationDtlResponse;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.comment.Comment;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
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
class LocationServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationServiceImpl locationService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private FriendRepository friendRepository;


    @BeforeEach
    void createMember() {
        Member member = memberRepository.save(Member.createMember(RegisterFormDto.createForTest("이병민", "강원도 원주시", "asdf@asdf.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("손흥민", "서울 강남구", "zxcv@zxcv.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("장원유", "대만 산총구", "yunni@yunni.com", "1234"), passwordEncoder));
    }

    @DisplayName("비로그인 유저가 장소 디테일 정보를 요청시 그 장소 디테일 정보를 가져온다")
    @Test
    void getLocationDtl(){
        //given
        String locationId = "C1_379000000A_001572";

        //when
        LocationDtlResponse response = locationService.getLocationDtl(locationId);

        //then
        assertThat(response.getName()).isEqualTo("西門町");
     }

    @DisplayName("비로그인 유저가 장소 디테일 정보를 요청시 요청 locationId 에 맞는 장소가 존재하지 않으면 , will throw LocationNotExistException")
    @Test
    void getLocationDtlWithoutLoginWithNotExistLocationId(){
        //given
        String locationId = "C1_379000000A_999999";

        //when//then
        assertThatThrownBy(() -> locationService.getLocationDtl(locationId))
                .isInstanceOf(com.dbproject.exception.LocationNotExistException.class)
                .hasMessage("Location 이 존재하지 않습니다.");
    }
    @DisplayName("비로그인 유저가 장소 디테일 정보를 요청시 이미 추가된 댓글들을 함께 가져온다 ")
    @Test
    void getLocationDtlAndCommentListWithNoLoginUser(){

        //given
        String locationId = "C1_379000000A_001572";
        addComment(locationId);

        //when
        LocationDtlResponse response = locationService.getLocationDtl(locationId);


        //then
        assertThat(response.getName()).isEqualTo("西門町");
        assertThat(response.getCommentDtoList()).hasSize(1);
        assertThat(response.getCommentDtoList().get(0).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(response.getCommentDtoList().get(0).getName()).isEqualTo("손흥민");
        assertThat(response.getCommentDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
    }

    @DisplayName("비로그인 유저가 장소 디테일 정보를 요청시 친구 목록은 null 이다")
    @Test
    void getLocationDtlWithNullFriendDtoList(){

        //given
        String locationId = "C1_379000000A_001572";

        //when
        LocationDtlResponse response = locationService.getLocationDtl(locationId);

        //then
        assertThat(response.getName()).isEqualTo("西門町");
        assertThat(response.getFriendDtoList().isEmpty()).isEqualTo(true);
    }

    private void addComment(String locationId) {
        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(locationId);
        Comment comment = Comment.create(content, member, location, rating);
        commentRepository.save(comment);
    }

    @DisplayName("로그인 유저가 장소 디테일 정보를 요청시 이미 즐겨찾기 리스트에 추가된 장소인지 확인하고 " +
            "이미 저장된 즐겨찾기 장소인 경우 isSave 를 true 로 저장하고 반환한다")
    @Test
    void checkSavedFavoriteLocationTrue(){
        //given
        createFavoriteLocation("C1_379000000A_001572");

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser("C1_379000000A_001572", "zxcv@zxcv.com");

        //then
        assertThat(locationDtlResponse.isSaved()).isEqualTo(true);
    }

    private void createFavoriteLocation(String locationId) {
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String memo = "메모 1 입니다.";
        Location location = locationRepository.findByLocationId(locationId);
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);
    }


    @DisplayName("로그인 유저가 장소 디테일 정보를 요청시 이미 즐겨찾기 리스트에 추가된 장소인지 확인하고 " +
            "즐겨찾기에 저장되지 않은 장소인 경우 isSave 를 false 로 저장하고 반환한다")
    @Test
    void checkSavedFavoriteLocationFalse(){

        //given

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser("C1_379000000A_001572","zxcv@zxcv.com");

        //then
        assertThat(locationDtlResponse.isSaved()).isEqualTo(false);
    }

    @DisplayName("로그인 유저가 장소 디테일 정보를 요청시 그 장소 디테일 정보를 가져온다")
    @Test
    void getLocationDtlWithLoginUser(){
        //given
        String locationId = "C1_379000000A_001572";
        String email = "zxcv@zxcv.com";
        createFavoriteLocation(locationId);
        addComment(locationId);

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,email);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
    }

    @DisplayName("로그인 유저가 장소 디테일 정보를 요청시 요청 locationId 에 맞는 장소가 존재하지 않으면 , will throw LocationNotExistException")
    @Test
    void getLocationDtlWithNotExistLocationId(){
        //given
        String locationId = "C1_379000000A_999999";
        String email = "zxcv@zxcv.com";

        //when//then
        assertThatThrownBy(() -> locationService.getLocationDtlWithAuthUser(locationId,email))
                .isInstanceOf(com.dbproject.exception.LocationNotExistException.class)
                .hasMessage("Location 이 존재하지 않습니다.");
    }

    @DisplayName("로그인 유저가 추천 장소의 Id 를 주면 그 장소와 댓글을 가져온다")
    @Test
    void getLocationDtlAndCommentListWithLoginUser(){
        //given
//        login user = son
        String locationId = "C1_379000000A_001572";
        String email = "zxcv@zxcv.com";
        createFavoriteLocation(locationId);
        addComment(locationId);

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,email);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
        assertThat(locationDtlResponse.getCommentDtoList()).hasSize(1);
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
    }

    @DisplayName("로그인 유저가 추천 장소의 Id 를 주면 그 장소와 친구 목록을 가져온다")
    @Test
    void getLocationDtlAndFriendListWithLoginUser(){
        //given
//        login user = son
        String locationId = "C1_379000000A_001572";
        String email = "zxcv@zxcv.com";
        createFavoriteLocation(locationId);
        createFriend("asdf@asdf.com", "zxcv@zxcv.com");
        createFriend("yunni@yunni.com", "zxcv@zxcv.com");

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,email);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
        assertThat(locationDtlResponse.getFriendDtoList()).hasSize(2);
        assertThat(locationDtlResponse.getFriendDtoList().get(0).getFriendEmail()).isEqualTo("asdf@asdf.com");
        assertThat(locationDtlResponse.getFriendDtoList().get(1).getFriendEmail()).isEqualTo("yunni@yunni.com");
    }

    @DisplayName("로그인 유저가 추천 장소의 Id 를 주면 그 추천 장소와 댓글과 친구 리스트 가져온다")
    @Test
    void getLocationDtlAndCommentListAndFriendListWithLoginUser(){
        //given
        String locationId = "C1_379000000A_001572";
        String email = "zxcv@zxcv.com";
        createFavoriteLocation(locationId);
        addComment(locationId);
        createFriend("asdf@asdf.com", "zxcv@zxcv.com");
        createFriend("yunni@yunni.com", "zxcv@zxcv.com");

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,email);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
        assertThat(locationDtlResponse.getCommentDtoList()).hasSize(1);
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(locationDtlResponse.getFriendDtoList()).hasSize(2);
        assertThat(locationDtlResponse.getFriendDtoList().get(0).getFriendEmail()).isEqualTo("asdf@asdf.com");
        assertThat(locationDtlResponse.getFriendDtoList().get(1).getFriendEmail()).isEqualTo("yunni@yunni.com");
    }

    private void createFriend(String requesterEmail, String respondentEmail) {
        Member requester = memberRepository.findByEmail(requesterEmail);
        Member respondent = memberRepository.findByEmail(respondentEmail);

        Friend friend = Friend.createFriend(requester, respondent);
        Friend friendReversed = Friend.createFriend(respondent,requester);

        friendRepository.save(friend);
        friendRepository.save(friendReversed);
    }
}