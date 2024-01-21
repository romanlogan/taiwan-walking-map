package com.dbproject.api.myPage.hangOut;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class HangOutService {

    private final HangOutRepository hangOutRepository;

    public void inviteHangOut(InviteHangOutRequest inviteHangOutRequest, String email) {




    }


}
