package com.dbproject.repository;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
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
import static org.hamcrest.Matchers.nullValue;


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

    @DisplayName("get favorite location list by Paging")
    @Test
    void  getFavoriteLocationList(){
        //given
        Pageable pageable = PageRequest.of(0, 5 );
        String email = "zxcv@zxcv.com";
        Long favoriteLocationId1 = createAndSaveFavoriteLocation(email,"C1_379000000A_001572");

        //when
        Page<FavoriteLocationDto> favoriteListPage = favoriteRepository.getFavoriteLocationListPage(pageable, email);

        //then
        assertThat(favoriteListPage.getContent().get(0).getName()).isEqualTo("西門町");
        assertThat(favoriteListPage.getContent().get(0).getFavoriteLocationId()).isEqualTo(favoriteLocationId1);
     }

    private Long createAndSaveFavoriteLocation(String email, String locationId) {

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모입니다.";

        return favoriteRepository.save(FavoriteLocation.of(member, location, memo)).getId();
    }


    @DisplayName("When 7 favoriteLocations are saved and page size is 5, there are 5 favoriteLocations on the first page. ")
    @Test
    void  getFirstFavoriteLocationPage(){
        //given
        Pageable pageable = PageRequest.of(0, 5 );
        String email = "zxcv@zxcv.com";
        Long favoriteLocationId1 = createAndSaveFavoriteLocation(email,"C1_379000000A_001572");
        Long favoriteLocationId2 = createAndSaveFavoriteLocation(email,"C1_379000000A_001573");
        Long favoriteLocationId3 = createAndSaveFavoriteLocation(email,"C1_379000000A_001574");
        Long favoriteLocationId4 = createAndSaveFavoriteLocation(email,"C1_379000000A_001575");
        Long favoriteLocationId5 = createAndSaveFavoriteLocation(email,"C1_379000000A_001577");
        Long favoriteLocationId6 = createAndSaveFavoriteLocation(email,"C1_379000000A_001579");
        Long favoriteLocationId7 = createAndSaveFavoriteLocation(email,"C1_379000000A_001580");

        //when
        Page<FavoriteLocationDto> favoriteListPage = favoriteRepository.getFavoriteLocationListPage(pageable, email);

        //then
        assertThat(favoriteListPage.getContent().size()).isEqualTo(5);
        assertThat(favoriteListPage.getContent().get(0).getName()).isEqualTo("西門町");
        assertThat(favoriteListPage.getContent().get(0).getFavoriteLocationId()).isEqualTo(favoriteLocationId1);
        assertThat(favoriteListPage.getContent().get(1).getName()).isEqualTo("信義商圈");
        assertThat(favoriteListPage.getContent().get(1).getFavoriteLocationId()).isEqualTo(favoriteLocationId2);
        assertThat(favoriteListPage.getContent().get(2).getName()).isEqualTo("台北市建國假日玉市");
        assertThat(favoriteListPage.getContent().get(2).getFavoriteLocationId()).isEqualTo(favoriteLocationId3);
        assertThat(favoriteListPage.getContent().get(3).getName()).isEqualTo("光華玉市");
        assertThat(favoriteListPage.getContent().get(3).getFavoriteLocationId()).isEqualTo(favoriteLocationId4);
        assertThat(favoriteListPage.getContent().get(4).getName()).isEqualTo("建國假日花市");
        assertThat(favoriteListPage.getContent().get(4).getFavoriteLocationId()).isEqualTo(favoriteLocationId5);
    }

    @DisplayName("When 7 favoriteLocations are saved and page size is 5, there are 2 favoriteLocations on the second page. ")
    @Test
    void  getSecondFavoriteLocationPage(){
        //given
        Pageable pageable = PageRequest.of(1, 5 );
        String email = "zxcv@zxcv.com";
        Long favoriteLocationId1 = createAndSaveFavoriteLocation(email,"C1_379000000A_001572");
        Long favoriteLocationId2 = createAndSaveFavoriteLocation(email,"C1_379000000A_001573");
        Long favoriteLocationId3 = createAndSaveFavoriteLocation(email,"C1_379000000A_001574");
        Long favoriteLocationId4 = createAndSaveFavoriteLocation(email,"C1_379000000A_001575");
        Long favoriteLocationId5 = createAndSaveFavoriteLocation(email,"C1_379000000A_001577");
        Long favoriteLocationId6 = createAndSaveFavoriteLocation(email,"C1_379000000A_001579");
        Long favoriteLocationId7 = createAndSaveFavoriteLocation(email,"C1_379000000A_001580");

        //when
        Page<FavoriteLocationDto> favoriteListPage = favoriteRepository.getFavoriteLocationListPage(pageable, email);

        //then
        assertThat(favoriteListPage.getContent().size()).isEqualTo(2);
        assertThat(favoriteListPage.getContent().get(0).getName()).isEqualTo("四平陽光商圈");
        assertThat(favoriteListPage.getContent().get(0).getFavoriteLocationId()).isEqualTo(favoriteLocationId6);
        assertThat(favoriteListPage.getContent().get(1).getName()).isEqualTo("龍山寺地下街");
        assertThat(favoriteListPage.getContent().get(1).getFavoriteLocationId()).isEqualTo(favoriteLocationId7);
    }

    @DisplayName("When 7 favoriteLocations are saved and page size is 5, there are empty on the third page. ")
    @Test
    void  getThirdFavoriteLocationPage(){
        //given
        Pageable pageable = PageRequest.of(2, 5 );
        String email = "zxcv@zxcv.com";
        Long favoriteLocationId1 = createAndSaveFavoriteLocation(email,"C1_379000000A_001572");
        Long favoriteLocationId2 = createAndSaveFavoriteLocation(email,"C1_379000000A_001573");
        Long favoriteLocationId3 = createAndSaveFavoriteLocation(email,"C1_379000000A_001574");
        Long favoriteLocationId4 = createAndSaveFavoriteLocation(email,"C1_379000000A_001575");
        Long favoriteLocationId5 = createAndSaveFavoriteLocation(email,"C1_379000000A_001577");
        Long favoriteLocationId6 = createAndSaveFavoriteLocation(email,"C1_379000000A_001579");
        Long favoriteLocationId7 = createAndSaveFavoriteLocation(email,"C1_379000000A_001580");

        //when
        Page<FavoriteLocationDto> favoriteListPage = favoriteRepository.getFavoriteLocationListPage(pageable, email);

        //then
        assertThat(favoriteListPage.getContent().isEmpty()).isEqualTo(true);
    }



}