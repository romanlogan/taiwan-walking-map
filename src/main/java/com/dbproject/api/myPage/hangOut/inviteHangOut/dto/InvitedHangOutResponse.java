package com.dbproject.api.myPage.hangOut.inviteHangOut.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InvitedHangOutResponse {
//  나중에 response 가 어떻게 바뀔지 모르니 지금은 list 하나만 있더라도 response 로 감싸자

    List<InvitedHangOutDto> invitedHangOutDtoList = new ArrayList<>();


}
