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

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("zxcv@zxcv.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);


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

    @DisplayName("비로그인 유저가 추천 장소의 Id 를 주면 그 추천 장소를 가져온다")
    @Test
    void getLocationDtl(){
        //given
        String locationId = "C1_379000000A_001572";

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtl(locationId);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
     }


    @DisplayName("로그인 유저가 즐겨찾기 리스트에 추가된 장소인지 확인하고 true 를 저장한다")
    @Test
    void checkSavedFavoriteLocationTrue(){
        //given
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        String memo = "메모 1 입니다.";
        Location location = locationRepository.findByLocationId(locationId);
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);


        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,member.getEmail());

        //then
        assertThat(locationDtlResponse.isSaved()).isEqualTo(true);
    }


    @DisplayName("로그인 유저가 즐겨찾기 리스트에 추가된 장소인지 확인하고 false 를 저장한다")
    @Test
    void checkSavedFavoriteLocationFalse(){

        //given
        String locationId = "C1_379000000A_001572";

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtl(locationId);

        //then
        assertThat(locationDtlResponse.isSaved()).isEqualTo(false);
    }

    private void createComment() {
        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(locationId);

        Comment comment = Comment.createComment(content, member, location, rating);
        commentRepository.save(comment);
    }

    @DisplayName("비로그인 유저가 추천 장소 를 가져올때 댓글은 1개 이다")
    @Test
    void getLocationDtlAndCommentListWithNoLoginUser(){

        //given
        String locationId = "C1_379000000A_001572";
        createComment();

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtl(locationId);


        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
        assertThat(locationDtlResponse.getCommentDtoList()).hasSize(1);
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getContent()).isEqualTo("댓글1 입니다.");
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getName()).isEqualTo("손흥민");
        assertThat(locationDtlResponse.getCommentDtoList().get(0).getEmail()).isEqualTo("zxcv@zxcv.com");
    }


    @DisplayName("로그인 유저가 추천 장소의 Id 를 주면 그 추천 장소와 댓글들을 가져온다")
    @Test
    void getLocationDtlAndCommentListWithLoginUser(){
        //given
        String locationId = "C1_379000000A_001572";
        String email = "zxcv@zxcv.com";
        createFavoriteLocation(locationId);
        createComment();

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,email);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
        assertThat(locationDtlResponse.getCommentDtoList()).hasSize(1);
    }

    private void createFavoriteLocation(String locationId) {
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String memo = "메모 1 입니다.";
        Location location = locationRepository.findByLocationId(locationId);
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);
    }



    @DisplayName("로그인 유저가 추천 장소의 Id 를 주면 그 추천 장소와 댓글과 친구 리스트 가져온다")
    @Test
    void getLocationDtlAndCommentListAndFriendListWithLoginUser(){
        //given
//        login user = son
        String locationId = "C1_379000000A_001572";
        String email = "zxcv@zxcv.com";
        createFavoriteLocation(locationId);
        createComment();
        createFriend("qwer@qwer.com", "zxcv@zxcv.com");
        createFriend("yunni@yunni.com", "zxcv@zxcv.com");

        //when
        LocationDtlResponse locationDtlResponse = locationService.getLocationDtlWithAuthUser(locationId,email);

        //then
        assertThat(locationDtlResponse.getName()).isEqualTo("西門町");
        assertThat(locationDtlResponse.getCommentDtoList()).hasSize(1);
        assertThat(locationDtlResponse.getFriendDtoList()).hasSize(2);
        assertThat(locationDtlResponse.getFriendDtoList().get(0).getFriendEmail()).isEqualTo("qwer@qwer.com");
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