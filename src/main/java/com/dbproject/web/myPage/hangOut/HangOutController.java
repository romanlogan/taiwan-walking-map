package com.dbproject.web.myPage.hangOut;

import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutService;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class HangOutController {

    private final InviteHangOutService hangOutService;

    @PostMapping("/inviteHangOut")
    public ResponseEntity inviteHangOut(@RequestBody InviteHangOutRequest inviteHangOutRequest,
                                        Principal principal) {


        Long id = hangOutService.inviteHangOut(inviteHangOutRequest, principal.getName());


        return new ResponseEntity(1L, HttpStatus.OK);
    }

    @GetMapping("/inviteHangOutList")
    public String getInviteHangOutList(Principal principal, Model model) {

        List<InviteHangOut> invitedHangOutList = hangOutService.getInviteHangOutList(principal.getName());

        model.addAttribute("invitedHangOutList", invitedHangOutList);
        return "/myPage/inviteHangOutList";
    }
}
