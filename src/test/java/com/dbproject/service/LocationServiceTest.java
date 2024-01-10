package com.dbproject.service;

import com.dbproject.dto.LocationDtlResponse;
import com.dbproject.dto.RecLocationListRequest;
import com.dbproject.dto.RecLocationListResponse;
import com.dbproject.dto.RegisterFormDto;
import com.dbproject.entity.Comment;
import com.dbproject.entity.FavoriteLocation;
import com.dbproject.entity.Location;
import com.dbproject.entity.Member;
import com.dbproject.repository.CommentRepository;
import com.dbproject.repository.FavoriteRepository;
import com.dbproject.repository.LocationRepository;
import com.dbproject.repository.MemberRepository;
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

import java.util.List;

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
    private LocationService locationService;

    @Autowired
    private CommentRepository commentRepository;

    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("zxcv@zxcv.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
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





    private void createComment() {
        int rating = 5;
        String content = "댓글1 입니다.";
        String email = "zxcv@zxcv.com";
        String locationId = "C1_379000000A_001572";

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(locationId);

        Comment comment = Comment.createComment(content, rating, member, location);
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
        assertThat(locationDtlResponse.getCommentList()).hasSize(1);
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
        assertThat(locationDtlResponse.getCommentList()).hasSize(1);
    }

    private void createFavoriteLocation(String locationId) {
        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String memo = "메모 1 입니다.";
        Location location = locationRepository.findByLocationId(locationId);
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);
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
}