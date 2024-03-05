package com.dbproject.api.myPage.hangOut;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.hangOut.hangOut.HangOut;
import com.dbproject.api.hangOut.hangOut.repository.HangOutRepository;
import com.dbproject.api.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.api.hangOut.inviteHangOut.repository.InviteHangOutRepository;
import com.dbproject.api.hangOut.inviteHangOut.dto.InviteHangOutRequest;
import com.dbproject.api.hangOut.inviteHangOut.service.InviteHangOutServiceImpl;
import com.dbproject.api.hangOut.inviteHangOut.dto.AcceptInvitedHangOutRequest;
import com.dbproject.api.hangOut.inviteHangOut.dto.InviteHangOutFromLocRequest;
import com.dbproject.api.hangOut.inviteHangOut.dto.InvitedHangOutResponse;
import com.dbproject.api.hangOut.inviteHangOut.dto.RejectInvitedHangOutRequest;
import com.dbproject.constant.InviteHangOutStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.dbproject.constant.InviteHangOutStatus.REJECTED;
import static com.dbproject.constant.InviteHangOutStatus.WAITING;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class InviteHangOutServiceTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private InviteHangOutRepository inviteHangOutRepository;

    @Autowired
    private InviteHangOutServiceImpl inviteHangOutService;

    @Autowired
    private HangOutRepository hangOutRepository;



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
        registerFormDto2.setEmail("asdf@asdf.com");
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

        String locationId = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(locationId);
        String memo = "메모 1 입니다.";
        FavoriteLocation favoriteLocation = new FavoriteLocation(member2, location, memo);
        favoriteRepository.save(favoriteLocation);

    }

    @DisplayName("InviteHangOutRequest 으로 친구를 HangOut 에 초대합니다")
    @Test
    void inviteHangOut() {

        //given
        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId("C1_379000000A_001572");
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";
        String friendEmail = "yunni@yunni.com";
        String myEmail = "asdf@asdf.com";

        InviteHangOutRequest inviteHangOutRequest = new InviteHangOutRequest(favoriteLocation.getId(), departDateTime, message, friendEmail);

        //when
        Long id = inviteHangOutService.inviteHangOut(inviteHangOutRequest, myEmail);

        //then
        List<InviteHangOut> InviteHangOutList = inviteHangOutRepository.findAll();
        assertThat(InviteHangOutList).size().isEqualTo(1);
        assertThat(InviteHangOutList.get(0).getMessage()).isEqualTo("message 1");
        assertThat(InviteHangOutList.get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(InviteHangOutList.get(0).getInviteHangOutStatus()).isEqualTo(InviteHangOutStatus.WAITING);
        assertThat(InviteHangOutList.get(0).getRequester().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(InviteHangOutList.get(0).getRespondent().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(InviteHangOutList.get(0).getLocation().getName()).isEqualTo("西門町");

    }


    // 친구 목록에서 즐겨찾기 장소로 초대
    @DisplayName("처음 리스트 페이지로 들어왔을때 이메일로 초대 받은 목록 가져옵니다, 처음 들어왔으므로 장소 정보는 없습니다, 그러므로  optionalInviteHangOutId 은 null ")
    @Test
    void getInvitedHangOutListWithNullId(){

        //given
        Optional<Long> optionalInviteHangOutId = Optional.empty();
        String myEmail = "asdf@asdf.com";
        String friendEmail = "yunni@yunni.com";
        Member friend = memberRepository.findByEmail(friendEmail);
        Member me = memberRepository.findByEmail(myEmail);
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        String memo = "memo 1";


        // 1. 유니가 장소를 즐겨찾기에 추가
        FavoriteLocation favoriteLocation = FavoriteLocation.createFavoriteLocation(friend, location, memo);
        FavoriteLocation savedFavoriteLocation = favoriteRepository.save(favoriteLocation);

        // 2. 즐겨찾기에 추가된 장소를 나에게 초대
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";

        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(message, departDateTime, savedFavoriteLocation.getLocation(), friend, me, InviteHangOutStatus.WAITING);
        inviteHangOutRepository.save(inviteHangOut);

        //when
        InvitedHangOutResponse invitedHangOutResponse = inviteHangOutService.getInvitedHangOutList(myEmail, optionalInviteHangOutId);

        //then
        assertThat(invitedHangOutResponse.getInviteHangOutLocationDto().getLocationId()).isNull();
        assertThat(invitedHangOutResponse.getInviteHangOutLocationDto().getName()).isNull();
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList()).hasSize(1);
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList().get(0).getLocationName()).isEqualTo("西門町");
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList().get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList().get(0).getMessage()).isEqualTo(message);
     }


    // 친구 목록에서 즐겨찾기 장소로 초대
    @DisplayName("이메일로 초대 받은 목록 가져오고 optionalInviteHangOutId 로 초대받은 장소 정보를 가져온다")
    @Test
    void getInvitedHangOutList(){

        //given
        String myEmail = "asdf@asdf.com";
        String friendEmail = "yunni@yunni.com";
        Member friend = memberRepository.findByEmail(friendEmail);
        Member me = memberRepository.findByEmail(myEmail);
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        String memo = "memo 1";


        // 1. 유니가 장소를 즐겨찾기에 추가
        FavoriteLocation favoriteLocation = FavoriteLocation.createFavoriteLocation(friend, location, memo);
        FavoriteLocation savedFavoriteLocation = favoriteRepository.save(favoriteLocation);

        // 2. 즐겨찾기에 추가된 장소를 나에게 초대
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";

        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(message, departDateTime, savedFavoriteLocation.getLocation(), friend, me, InviteHangOutStatus.WAITING);
        InviteHangOut savedInvitedHangOut = inviteHangOutRepository.save(inviteHangOut);

        Optional<Long> optionalInviteHangOutId2 = Optional.ofNullable(savedInvitedHangOut.getId());

        //when
        InvitedHangOutResponse invitedHangOutResponse = inviteHangOutService.getInvitedHangOutList(myEmail, optionalInviteHangOutId2);


        //then
        assertThat(invitedHangOutResponse.getInviteHangOutLocationDto().getLocationId()).isEqualTo("C1_379000000A_001572");
        assertThat(invitedHangOutResponse.getInviteHangOutLocationDto().getName()).isEqualTo("西門町");
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList()).hasSize(1);
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList().get(0).getLocationName()).isEqualTo("西門町");
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList().get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(invitedHangOutResponse.getInvitedHangOutDtoList().get(0).getMessage()).isEqualTo(message);
    }


    @DisplayName("HangOut 요청을 수락합니다")
    @Test
    void acceptInvitedHangOut(){

        //given
        Member friend = memberRepository.findByEmail("yunni@yunni.com");
        Member me = memberRepository.findByEmail("asdf@asdf.com");
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";

        //약속 초대 및 생성
        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(message, departDateTime, location , friend, me, InviteHangOutStatus.WAITING);
        InviteHangOut savedInvitedHangOut = inviteHangOutRepository.save(inviteHangOut);

        AcceptInvitedHangOutRequest acceptInvitedHangOutRequest = new AcceptInvitedHangOutRequest(savedInvitedHangOut.getId());

        //when
        inviteHangOutService.acceptInvitedHangOut(acceptInvitedHangOutRequest);

        //then
        List<InviteHangOut> inviteHangOutList = inviteHangOutRepository.findAll();
        List<HangOut> hangOutList = hangOutRepository.findAll();

        assertThat(inviteHangOutList).hasSize(0);
        assertThat(hangOutList).hasSize(2);
        assertThat(hangOutList.get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(hangOutList.get(0).getRequester().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(hangOutList.get(0).getRespondent().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(hangOutList.get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(hangOutList.get(1).getRequester().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(hangOutList.get(1).getRespondent().getEmail()).isEqualTo("yunni@yunni.com");

    }


    @DisplayName("HangOut 요청을 거절합니다")
    @Test
    void rejectInvitedHangOut(){

        //given
        Member friend = memberRepository.findByEmail("yunni@yunni.com");
        Member me = memberRepository.findByEmail("asdf@asdf.com");
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";

        //약속 초대 및 생성
        InviteHangOut inviteHangOut = InviteHangOut.createHangOut(message, departDateTime, location , friend, me, InviteHangOutStatus.WAITING);
        InviteHangOut savedInvitedHangOut = inviteHangOutRepository.save(inviteHangOut);

        RejectInvitedHangOutRequest rejectInvitedHangOutRequest = new RejectInvitedHangOutRequest(savedInvitedHangOut.getId());

        //when
        inviteHangOutService.rejectInvitedHangOut(rejectInvitedHangOutRequest);

        //then
        List<InviteHangOut> inviteHangOutList = inviteHangOutRepository.findAll();

        assertThat(inviteHangOutList).hasSize(1);
        assertThat(inviteHangOutList.get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(inviteHangOutList.get(0).getRequester().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(inviteHangOutList.get(0).getRespondent().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(inviteHangOutList.get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(inviteHangOutList.get(0).getInviteHangOutStatus()).isEqualTo(REJECTED);
    }

    @DisplayName("장소 페이지에서 친구에게 HangOut 요청을 보냅니다.")
    @Test
    void inviteFromLocationPage() {

        //given
        Member friend = memberRepository.findByEmail("yunni@yunni.com");
        Member me = memberRepository.findByEmail("asdf@asdf.com");
        Location location = locationRepository.findByLocationId("C1_379000000A_001572");
        LocalDateTime departDateTime = LocalDateTime.now();
        String message = "message 1";

        InviteHangOutFromLocRequest inviteHangOutFromLocRequest = new InviteHangOutFromLocRequest(location.getLocationId(), friend.getEmail(), departDateTime, message);

        //when
        inviteHangOutService.inviteFromLocationPage(inviteHangOutFromLocRequest, me.getEmail());

        //then
        List<InviteHangOut> inviteHangOutList = inviteHangOutRepository.findAll();
        assertThat(inviteHangOutList).hasSize(1);
        assertThat(inviteHangOutList.get(0).getLocation().getName()).isEqualTo("西門町");
        assertThat(inviteHangOutList.get(0).getRequester().getEmail()).isEqualTo("asdf@asdf.com");
        assertThat(inviteHangOutList.get(0).getRespondent().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(inviteHangOutList.get(0).getDepartDateTime()).isEqualTo(departDateTime);
        assertThat(inviteHangOutList.get(0).getInviteHangOutStatus()).isEqualTo(WAITING);

    }


}
















