package com.dbproject.repository;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.favorite.FavoriteListResponse;
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
class FavoriteRepositoryCustomImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;

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

    @DisplayName("즐겨찾기 리스트를 페이징하여 반환합니다")
    @Test
    void  getFavoriteLocationList(){
        //given
        Pageable pageable = PageRequest.of(0, 5 );
        String email = "zxcv@zxcv.com";

        Member member = memberRepository.findByEmail("zxcv@zxcv.com");
        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모 1 입니다.";
        FavoriteLocation savedFavoriteLocation = new FavoriteLocation(member, location, memo);
        favoriteRepository.save(savedFavoriteLocation);

        //when
        Page<FavoriteListResponse> favoriteListResponsePage = favoriteRepository.getFavoriteLocationListPage(pageable, email);

        //then
        assertThat(favoriteListResponsePage.getContent().get(0).getName()).isEqualTo("西門町");

     }


}