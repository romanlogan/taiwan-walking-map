package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitedHangOutResponse {
//  나중에 response 가 어떻게 바뀔지 모르니 지금은 list 하나만 있더라도 response 로 감싸자


//    Waiting 리스트가 그렇게 많을것 같지도 않고 페이징보단 스크롤 형태로 가져가고 싶어서 페이지 사용안하고 리스트로 가져옴
    List<InvitedHangOutDto> invitedHangOutDtoList = new ArrayList<>();

    public InvitedHangOutResponse() {
    }

    public InvitedHangOutResponse(List<InvitedHangOutDto> invitedHangOutDtoList) {
        this.invitedHangOutDtoList = invitedHangOutDtoList;
    }
}
