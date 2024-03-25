package com.dbproject.api.invitePlan.invitePlanMember.repository;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.invitePlan.InvitePlan;
import com.dbproject.api.invitePlan.dto.InvitePlanLocationRequest;
import com.dbproject.api.invitePlan.dto.InvitePlanRequest;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.invitePlan.invitePlanMember.dto.InvitePlanMemberRequest;
import com.dbproject.api.invitePlan.repository.InvitePlanRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.Member;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.constant.PlanPeriod;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class InvitePlanMemberRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private FavoriteRepository favoriteRepository;

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
        registerFormDto1.setPhoneNumber("0912345678");
        registerFormDto1.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto1.setGender(1);
        registerFormDto1.setAcceptReceiveAdvertising(true);

        Member member1 = Member.createMember(registerFormDto1, passwordEncoder);
        memberRepository.save(member1);

        RegisterFormDto registerFormDto2 = new RegisterFormDto();
        registerFormDto2.setName("이병민");
        registerFormDto2.setAddress("강원도 원주시");
        registerFormDto2.setEmail("asdf@asdf.com");
        registerFormDto2.setPassword("1234");
        registerFormDto2.setPhoneNumber("0912345678");
        registerFormDto2.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto2.setGender(1);
        registerFormDto2.setAcceptReceiveAdvertising(true);

        Member member2 = Member.createMember(registerFormDto2, passwordEncoder);
        memberRepository.save(member2);

        RegisterFormDto registerFormDto3 = new RegisterFormDto();
        registerFormDto3.setName("장원유");
        registerFormDto3.setAddress("대만 산총구");
        registerFormDto3.setEmail("yunni@yunni.com");
        registerFormDto3.setPassword("1234");
        registerFormDto3.setPhoneNumber("0912345678");
        registerFormDto3.setDateOfBirth(LocalDate.parse("1996-12-10"));
        registerFormDto3.setGender(1);
        registerFormDto3.setAcceptReceiveAdvertising(true);

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

    public InvitePlanRequest createRequest(String name, PlanPeriod planPeriod, String supply ,LocalDate departDate, Integer tripDay) {
//        String name = "lee's 3 days tainan trip";
//        PlanPeriod planPeriod = PlanPeriod.LONGTRIP;
//        String supply = "hair dryer, slipper, brush";
//        LocalDate departDate = LocalDate.of(2024, 3,20);
        LocalDate arriveDate = LocalDate.of(departDate.getYear(), departDate.getMonthValue(), departDate.getDayOfMonth() + tripDay);
        InvitePlanRequest request = new InvitePlanRequest(name, planPeriod, supply, departDate, arriveDate);

        return request;
    }

    public void setInvitePlanMemberList(InvitePlan savedInvitePlan, InvitePlanRequest request){
        List<InvitePlanMember> invitePlanMemberList = new ArrayList<>();
        for (InvitePlanMemberRequest memberRequest: request.getInvitePlanMemberRequestList()) {
            //0.Member 찾기
            String email = memberRequest.getFriendEmail();
            Member member = memberRepository.findByEmail(email);

            //1. InvitePlanMember 생성 및 저장
            InvitePlanMember invitePlanMember = InvitePlanMember.createInvitePlanMemberWithoutSupply(member, savedInvitePlan);
            invitePlanMemberRepository.save(invitePlanMember);

            //2. InvitePlanMemberList 에 InvitePlanMember  추가
            invitePlanMemberList.add(invitePlanMember);
        }
        savedInvitePlan.setInviteFriendList(invitePlanMemberList);
    }



    public void setLocationList(InvitePlan savedInvitePlan, InvitePlanRequest request) {

        List<Location> locationList = new ArrayList<>();
        for (InvitePlanLocationRequest locationRequest : request.getInvitePlanLocationRequestList()) {
            //            fetch 조인으로 바꿀 필요
            Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(locationRequest.getFavoriteLocationId()));
            FavoriteLocation favoriteLocation = optionalFavoriteLocation.get();
            Location location = favoriteLocation.getLocation();

//            Location location = locationRepository.findByLocationId();       //savedInvitePlan 이 id 가 0인데 이 아이디가 0인 savedInvitePlan 을 찾아르 수 없다
            locationList.add(location);
        }
        savedInvitePlan.setLocationList(locationList);
    }

    @DisplayName("email 로 초대받은 Plan 목록을 가져옵니다.")
    @Test
    void getInvitedPlanMemberListByEmail(){
        //given
        Member son = memberRepository.findByEmail("zxcv@zxcv.com");
        Member yunni = memberRepository.findByEmail("yunni@yunni.com");

        // create Request
        LocalDate departDate = LocalDate.of(2024, 3,20);
        LocalDate departDate2 = LocalDate.of(2024, 6,20);
        InvitePlanRequest request = createRequest("lee's 3 days tainan trip",
                PlanPeriod.LONGTRIP,
                "hair dryer, slipper, brush",
                departDate,
                3);
        setInvitePlanMemberRequestList(request);
        setInvitePlanLocationRequestList(request);

        InvitePlanRequest request2 = createRequest("lee's 7 days japan trip",
                PlanPeriod.LONGTRIP,
                "computer, ipad",
                departDate2,
                7);
        setInvitePlanMemberRequestList(request2);
        setInvitePlanLocationRequestList(request2);


        InvitePlan invitePlan = InvitePlan.createInvitePlan(request);
        InvitePlan saveInvitePlan = invitePlanRepository.save(invitePlan);
        setInvitePlanMemberList(saveInvitePlan,request);
        setLocationList(saveInvitePlan,request);

        InvitePlan invitePlan2 = InvitePlan.createInvitePlan(request2);
        InvitePlan saveInvitePlan2 = invitePlanRepository.save(invitePlan2);
        setInvitePlanMemberList(saveInvitePlan2,request2);
        setLocationList(saveInvitePlan2,request2);

        InvitePlanMember invitePlanMember = InvitePlanMember.createInvitePlanMemberWithoutSupply(son, saveInvitePlan);
        InvitePlanMember invitePlanMember2 = InvitePlanMember.createInvitePlanMemberWithoutSupply(yunni, saveInvitePlan);
        InvitePlanMember invitePlanMember3 = InvitePlanMember.createInvitePlanMemberWithoutSupply(son, saveInvitePlan2);
        InvitePlanMember invitePlanMember4 = InvitePlanMember.createInvitePlanMemberWithoutSupply(yunni, saveInvitePlan2);
        invitePlanMemberRepository.save(invitePlanMember);
        invitePlanMemberRepository.save(invitePlanMember2);
        invitePlanMemberRepository.save(invitePlanMember3);
        invitePlanMemberRepository.save(invitePlanMember4);

        //when
        List<InvitePlanMember> invitedPlanMemberList = invitePlanMemberRepository.getInvitedPlanMemberListByEmail("yunni@yunni.com");

        //then
        assertThat(invitedPlanMemberList.size()).isEqualTo(4);      //son 2 + yunni 2
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getName()).isEqualTo("lee's 3 days tainan trip");
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getDepartDate()).isEqualTo(LocalDate.of(2024,3,20));
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getArriveDate()).isEqualTo(LocalDate.of(2024,3,23));
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getInviteFriendList().size()).isEqualTo(2);
//        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getCreatedBy()).isEqualTo("asdf@asdf.com");       //plan 작성자를 created by 를 사용했더니 test 코드에서 작성자 검증이 안됨
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getInviteFriendList().get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(invitedPlanMemberList.get(0).getInvitePlan().getInviteFriendList().get(1).getMember().getEmail()).isEqualTo("yunni@yunni.com");
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getName()).isEqualTo("lee's 7 days japan trip");
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getPeriod()).isEqualTo(PlanPeriod.LONGTRIP);
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getDepartDate()).isEqualTo(LocalDate.of(2024,6,20));
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getArriveDate()).isEqualTo(LocalDate.of(2024,6,27));
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getInviteFriendList().size()).isEqualTo(2);
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getInviteFriendList().get(0).getMember().getEmail()).isEqualTo("zxcv@zxcv.com");
        assertThat(invitedPlanMemberList.get(1).getInvitePlan().getInviteFriendList().get(1).getMember().getEmail()).isEqualTo("yunni@yunni.com");
    }


}