package com.dbproject.api.invitePlan.dto;

import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.constant.PlanPeriod;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitePlanDto {

    private String name;
    private PlanPeriod period;
    private String supply;
    private LocalDate departDate;
    private LocalDate arriveDate;


//    List<FriendDto> friendDtoList = new ArrayList<>();
    List<InvitePlanMemberDto> invitePlanMemberDtoList = new ArrayList<>();

    // 내가 초대 받은 InvitePlan 의 목록을 보여주는것이므로 favoriteLocation 보다는 Location 사용
    List<LocationDto> locationDtoList = new ArrayList<>();

    public InvitePlanDto() {
    }

    public InvitePlanDto(String name, PlanPeriod period, String supply, LocalDate departDate, LocalDate arriveDate) {
        this.name = name;
        this.period = period;
        this.supply = supply;
        this.departDate = departDate;
        this.arriveDate = arriveDate;
    }
}
