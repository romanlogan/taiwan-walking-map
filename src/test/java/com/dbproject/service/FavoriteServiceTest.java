package com.dbproject.service;

import com.dbproject.api.favorite.FavoriteService;
import com.dbproject.web.favorite.AddFavoriteLocationRequest;
import com.dbproject.web.member.RegisterFormDto;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.member.Member;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
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


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FavoriteServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteService favoriteService;

    @BeforeEach
    void createMember() {

        RegisterFormDto registerFormDto = new RegisterFormDto();
        registerFormDto.setName("손흥민");
        registerFormDto.setAddress("서울 강남구");
        registerFormDto.setEmail("test@test.com");
        registerFormDto.setPassword("1234");

        Member member = Member.createMember(registerFormDto, passwordEncoder);
        memberRepository.save(member);
    }

    @DisplayName("locationId 와 유저 email 을 받아서 즐겨찾기 리스트에 추가한다")
    @Test
    void addFavoriteList(){

        //given
        String email = "test@test.com";
        String locationId = "C1_379000000A_001572";
        String memo = "메모 1 입니다.";
        AddFavoriteLocationRequest addFavoriteLocationRequest = new AddFavoriteLocationRequest(locationId, memo);

        //when
        favoriteService.addFavoriteList(addFavoriteLocationRequest, email);

        //then
        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findAll();
        assertThat(favoriteLocationList).hasSize(1);
        assertThat(favoriteLocationList.get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(favoriteLocationList.get(0).getMemo()).isEqualTo("메모 1 입니다.");
     }
}