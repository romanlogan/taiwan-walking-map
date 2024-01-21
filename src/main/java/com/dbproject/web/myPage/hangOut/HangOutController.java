package com.dbproject.web.myPage.hangOut;

import com.dbproject.api.myPage.hangOut.HangOutService;
import com.dbproject.api.myPage.hangOut.InviteHangOutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class HangOutController {

    private final HangOutService hangOutService;

    @PostMapping("/myPage/hangOut")
    public ResponseEntity inviteHangOut(@RequestBody InviteHangOutRequest inviteHangOutRequest,
                                        Principal principal) {



        Long id = hangOutService.inviteHangOut(inviteHangOutRequest,principal.getName());

        return new ResponseEntity(1L, HttpStatus.OK);
    }
}
