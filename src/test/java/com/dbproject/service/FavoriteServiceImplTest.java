package com.dbproject.service;

import com.dbproject.api.favorite.dto.*;
import com.dbproject.api.favorite.service.FavoriteServiceImpl;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.member.Member;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.exception.FavoriteLocationNotExistException;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class FavoriteServiceImplTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteServiceImpl favoriteService;

    @BeforeEach
    void createMember() {

        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("이병민", "강원도 원주시", "asdf@asdf.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("손흥민", "서울 강남구", "zxcv@zxcv.com", "1234"), passwordEncoder));
        memberRepository.save(Member.createMember(RegisterFormDto.createForTest("장원유", "대만 산총구", "yunni@yunni.com", "1234"), passwordEncoder));

    }

    @DisplayName("어떤 사용자도 즐겨찾기로 저장하지 않았으면 즐겨찾기 개수는 null 이다")
    @Test
    void CommentCountIsNullBeforeAddComment() {

        //when
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");

        //then
        assertThat(location.getFavoriteCount()).isNull();
    }


    @DisplayName("用locationId和email來，Location儲存在FavoriteList")
    @Test
    void addFavoriteList(){
        //given
        String email = "asdf@asdf.com";
        AddFavoriteLocationRequest request = AddFavoriteLocationRequest.create("C1_379000000A_001572", "메모 1 입니다.");

        //when
        Long id = favoriteService.addFavoriteList(request, email);

        //then
        FavoriteLocation favoriteLocation = favoriteRepository.findById(id).get();
        assertThat(favoriteLocation.getLocation().getName()).isEqualTo("西門町");
        assertThat(favoriteLocation.getMember().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(favoriteLocation.getMemo()).isEqualTo("메모 1 입니다.");
     }

    @DisplayName("儲存在FavoriteList時，如果已儲存過的地方，就return DuplicateFavoriteLocationException")
    @Test
    void addFavoriteListWithAddDuplicateFavoriteLocation(){
        //given
        String email = "asdf@asdf.com";
        AddFavoriteLocationRequest request = AddFavoriteLocationRequest.create("C1_379000000A_001572", "메모 1 입니다.");
        favoriteService.addFavoriteList(request, email);

        //when //then
        assertThatThrownBy(() -> favoriteService.addFavoriteList(request, email))
                .isInstanceOf(DuplicateFavoriteLocationException.class)
                .hasMessage("이미 등록된 장소 입니다.");
    }

    private Long addFavoriteLocation(String email, String locationId) {
        String memo = "메모 1 입니다.";
        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(locationId);

        FavoriteLocation favoriteLocation = FavoriteLocation.of(member, location, memo);
        favoriteRepository.save(favoriteLocation);
        location.increaseFavoriteCount();

        return favoriteLocation.getId();
    }


//    getPage

    @DisplayName("儲存6個FavoriteLocation後，getFavoriteLocationListPage時，第一頁有5個FavoriteLocation")
    @Test
    void getFirstPageInFavoriteLocationListPageAfterAddSixFavoriteLocation(){
        //given
        String email = "asdf@asdf.com";
        addFavoriteLocation(email,"C1_379000000A_001571");
        addFavoriteLocation(email,"C1_379000000A_001572");
        addFavoriteLocation(email,"C1_379000000A_001573");
        addFavoriteLocation(email,"C1_379000000A_001574");
        addFavoriteLocation(email,"C1_379000000A_001575");
//        1576 not exist
        addFavoriteLocation(email,"C1_379000000A_001577");

        Pageable pageable = PageRequest.of(0, 5);

        //when
        FavoriteLocationListResponse response = favoriteService.getFavoriteLocationList(pageable, email);

        //then
        Page<FavoriteLocationList> favoriteLocationPage = response.getFavoriteListResponsePage();
        assertThat(favoriteLocationPage.getTotalElements()).isEqualTo(6);
        assertThat(favoriteLocationPage.getTotalPages()).isEqualTo(2);
        assertThat(favoriteLocationPage.getContent().size()).isEqualTo(5);
        assertThat(favoriteLocationPage.getContent().get(0).getLocationId()).isEqualTo("C1_379000000A_001571");
        assertThat(favoriteLocationPage.getContent().get(1).getLocationId()).isEqualTo("C1_379000000A_001572");
        assertThat(favoriteLocationPage.getContent().get(2).getLocationId()).isEqualTo("C1_379000000A_001573");
        assertThat(favoriteLocationPage.getContent().get(3).getLocationId()).isEqualTo("C1_379000000A_001574");
        assertThat(favoriteLocationPage.getContent().get(4).getLocationId()).isEqualTo("C1_379000000A_001575");
    }

    @DisplayName("儲存6個FavoriteLocation後，getFavoriteLocationListPage時，第二頁只有1個FavoriteLocation")
    @Test
    void getSecondPageInFavoriteLocationListPageAfterAddSixFavoriteLocation(){
        //given
        String email = "asdf@asdf.com";
        addFavoriteLocation(email,"C1_379000000A_001571");
        addFavoriteLocation(email,"C1_379000000A_001572");
        addFavoriteLocation(email,"C1_379000000A_001573");
        addFavoriteLocation(email,"C1_379000000A_001574");
        addFavoriteLocation(email,"C1_379000000A_001575");
//        1576 not exist
        addFavoriteLocation(email,"C1_379000000A_001577");

        Pageable pageable = PageRequest.of(1, 5);

        //when
        FavoriteLocationListResponse response = favoriteService.getFavoriteLocationList(pageable, email);
        Page<FavoriteLocationList> favoriteLocationPage = response.getFavoriteListResponsePage();

        //then
        assertThat(favoriteLocationPage.getTotalElements()).isEqualTo(6);
        assertThat(favoriteLocationPage.getTotalPages()).isEqualTo(2);
        assertThat(favoriteLocationPage.getContent().size()).isEqualTo(1);
        assertThat(favoriteLocationPage.getContent().get(0).getLocationId()).isEqualTo("C1_379000000A_001577");
        assertThat(favoriteLocationPage.getContent().get(0).getName()).isEqualTo("建國假日花市");
    }


//     delete

     @DisplayName("刪除FavoriteLocation")
     @Test
     void deleteFavoriteLocation(){
         //given
         String email = "asdf@asdf.com";
         Long id = addFavoriteLocation(email, "C1_379000000A_001571");
         addFavoriteLocation(email,"C1_379000000A_001572");

         DeleteFavoriteLocationRequest request = new DeleteFavoriteLocationRequest(Math.toIntExact(id));

         //when
         favoriteService.deleteFavoriteLocation(request);

         //then
         List<FavoriteLocation> favoriteLocationListAfterDeleted = favoriteRepository.findAll();
         assertThat(favoriteLocationListAfterDeleted.size()).isEqualTo(1);
         assertThat(favoriteLocationListAfterDeleted.get(0).getLocation().getLocationId()).isEqualTo("C1_379000000A_001572");
    }


    @DisplayName("When deleting a favorite location that does not exist, return FavoriteLocationNotExistException")
    @Test
    void deleteNotExistFavoriteLocation(){
        //given
        DeleteFavoriteLocationRequest request = new DeleteFavoriteLocationRequest(1);

        //when //then
        assertThatThrownBy(() -> favoriteService.deleteFavoriteLocation(request))
                .isInstanceOf(FavoriteLocationNotExistException.class)
                .hasMessage("즐겨찾기 장소가 존재하지 않습니다.");
    }

    @DisplayName("decrease location's favorite count when delete FavoriteLocation ")
    @Test
    void decreaseFavoriteCount(){
        //given
        Long id = addFavoriteLocation("asdf@asdf.com", "C1_379000000A_001571");
        addFavoriteLocation("yunni@yunni.com","C1_379000000A_001571");

        DeleteFavoriteLocationRequest request = new DeleteFavoriteLocationRequest(Math.toIntExact(id));
        assertFavoriteCountIs(2,"yunni@yunni.com");

        //when
        favoriteService.deleteFavoriteLocation(request);

        //then
        assertFavoriteCountIs(1,"yunni@yunni.com");

    }

    private void assertFavoriteCountIs(Integer count,String email) {
        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationIdAndEmail("C1_379000000A_001571",email);
        assertThat(favoriteLocation.getLocation().getFavoriteCount()).isEqualTo(count);
    }

    @DisplayName("update memo of FavoriteLocation")
    @Test
    void updateMemo() {

        //givenf
        Long id = addFavoriteLocation("asdf@asdf.com", "C1_379000000A_001572");
        UpdateMemoRequest request = new UpdateMemoRequest(Math.toIntExact(id), "updated memo");

        //when
        favoriteService.updateMemo(request);

        //then
        FavoriteLocation favoriteLocation = favoriteRepository.findById(id).get();
        assertThat(favoriteLocation.getMemo()).isEqualTo("updated memo");
        assertThat(favoriteLocation.getLocation().getName()).isEqualTo("西門町");
    }

    @DisplayName("When update memo of favorite location that does not exist, return FavoriteLocationNotExistException")
    @Test
    void updateMemoWithNotExistFavoriteLocation(){
        //given
        UpdateMemoRequest request = new UpdateMemoRequest(1 , "updated memo");

        //when //then
        assertThatThrownBy(() -> favoriteService.updateMemo(request))
                .isInstanceOf(FavoriteLocationNotExistException.class)
                .hasMessage("즐겨찾기 장소가 존재하지 않습니다.");
    }
}
