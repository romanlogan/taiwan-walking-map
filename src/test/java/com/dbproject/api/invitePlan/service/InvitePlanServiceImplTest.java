package com.dbproject.api.invitePlan.service;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.api.invitePlan.invitePlanMember.repository.InvitePlanMemberRepository;
import com.dbproject.api.invitePlan.repository.InvitePlanRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.constant.InvitePlanStatus;
import com.dbproject.constant.PlanPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.PushBuilder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class InvitePlanServiceImplTest {


    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private InvitePlanService invitePlanService;

    @Autowired
    private InvitePlanRepository invitePlanRepository;

    @Autowired
    private InvitePlanMemberRepository invitePlanMemberRepository;



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


        String ximending = "C1_379000000A_001572";
        Location location = locationRepository.findByLocationId(ximending);
        String memo = "메모 1 입니다.";
        FavoriteLocation favoriteLocation = new FavoriteLocation(member2, location, memo);
        favoriteRepository.save(favoriteLocation);

        String taipei101 = "C1_379000000A_000217";
        Location location2 = locationRepository.findByLocationId(taipei101);
        String memo2 = "메모 1 입니다.";
        FavoriteLocation favoriteLocation2 = new FavoriteLocation(member2, location2, memo2);
        favoriteRepository.save(favoriteLocation2);

        String taipeidixiajie = "C1_379000000A_001591";
        Location location3 = locationRepository.findByLocationId(taipeidixiajie);
        String memo3 = "메모 1 입니다.";
        FavoriteLocation favoriteLocation3 = new FavoriteLocation(member2, location3, memo3);
        favoriteRepository.save(favoriteLocation3);
    }

    public void setInvitePlanMemberRequestList(InvitePlanRequest request){
        List<InvitePlanMemberRequest> invitePlanMemberRequestList = new ArrayList<>();
        InvitePlanMemberRequest invitePlanMemberRequest1 = new InvitePlanMemberRequest("zxcv@zxcv.com","computer");
        InvitePlanMemberRequest invitePlanMemberRequest2 = new InvitePlanMemberRequest("yunni@yunni.com","ipad");
        invitePlanMemberRequestList.add(invitePlanMemberRequest1);
        invitePlanMemberRequestList.add(invitePlanMemberRequest2);
        request.setInvitePlanMemberRequestList(invitePlanMemberRequestList);
    }

    public void setInvitePlanLocationRequestList(InvitePlanRequest request) {

        List<FavoriteLocation> favoriteLocationList = favoriteRepository.findByMemberEmail("asdf@asdf.com");

        List<InvitePlanLocationRequest> invitePlanLocationRequestList = new ArrayList<>();
        InvitePlanLocationRequest invitePlanLocationRequest1 = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocationList.get(0).getId()));
        InvitePlanLocationRequest invitePlanLocationRequest2 = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocationList.get(1).getId()));
        InvitePlanLocationRequest invitePlanLocationRequest3 = new InvitePlanLocationRequest(Math.toIntExact(favoriteLocationList.get(2).getId()));
        invitePlanLocationRequestList.add(invitePlanLocationRequest1);
        invitePlanLocationRequestList.add(invitePlanLocationRequest2);
        invitePlanLocationRequestList.add(invitePlanLocationRequest3);
        request.setInvitePlanLocationRequestList(invitePlanLocationRequestList);
    }

    public InvitePlanRequest createRequest() {
        String name = "lee's 3 days tainan trip";
        PlanPeriod planPeriod = PlanPeriod.LONGTRIP;
        String supply = "hair dryer, slipper, brush";
        LocalDate departDate = LocalDate.of(2024, 3,20);
        LocalDate arriveDate = LocalDate.of(2024, 3,22);
        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate, arriveDate);

        return request;
    }

    @DisplayName("create plan and invite friend to join the plan")
    @Test
    void invitePlan(){
        //given
        InvitePlanRequest request = createRequest();
        setInvitePlanMemberRequestList(request);
        setInvitePlanLocationRequestList(request);

        //when
        Long invitePlanId = invitePlanService.invitePlan(request);

        //then
        Optional<InvitePlan> savedInvitePlan = invitePlanRepository.findById(invitePlanId);
        InvitePlan invitePlan = savedInvitePlan.get();
        assertThat(invitePlan.getName()).isEqualTo("lee's 3 days tainan trip");
        assertThat(invitePlan.getSupply()).isEqualTo("hair dryer, slipper, brush");
        assertThat(invitePlan.getDepartDate()).isEqualTo("2024-03-20");
        assertThat(invitePlan.getArriveDate()).isEqualTo("2024-03-22");
        assertThat(invitePlan.getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(invitePlan.getInviteFriendList().size()).isEqualTo(2);
        assertThat(invitePlan.getInviteFriendList().get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
//        assertThat(invitePlan.getInviteFriendList().get(0).getSupply()).isEqualTo("computer");
        assertThat(invitePlan.getInviteFriendList().get(0).getInvitePlanStatus()).isEqualTo(InvitePlanStatus.WAITING);
        assertThat(invitePlan.getInviteFriendList().get(1).getMember().getEmail()).isEqualTo("yunni@yunni.com");
//        assertThat(invitePlan.getInviteFriendList().get(1).getSupply()).isEqualTo("ipad");
        assertThat(invitePlan.getInviteFriendList().get(1).getInvitePlanStatus()).isEqualTo(InvitePlanStatus.WAITING);

        assertThat(invitePlan.getLocationList().size()).isEqualTo(3);
        assertThat(invitePlan.getLocationList().get(0).getName()).isEqualTo("西門町");
        assertThat(invitePlan.getLocationList().get(1).getName()).isEqualTo("台北101");
        assertThat(invitePlan.getLocationList().get(2).getName()).isEqualTo("台北地下街");

    }



}