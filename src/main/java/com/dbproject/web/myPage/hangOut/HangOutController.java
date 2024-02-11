package com.dbproject.web.myPage.hangOut;

import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutService;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRequest;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.AcceptInvitedHangOutRequest;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.InviteHangOutLocationDto;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.InvitedHangOutResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
    public String getInviteHangOutList(@RequestParam(name = "inviteHangOutId") Optional<Long> inviteHangOutId,
                                       Principal principal, Model model) {


        InvitedHangOutResponse invitedHangOutResponse = hangOutService.getInvitedHangOutList(principal.getName(), inviteHangOutId);

        model.addAttribute("invitedHangOutResponse", invitedHangOutResponse);
        return "/myPage/inviteHangOutList";
    }

    @PostMapping("/acceptInvitedHangOut")
    public ResponseEntity acceptInvitedHangOut(@RequestBody AcceptInvitedHangOutRequest acceptInvitedHangOutRequest) {

        System.out.println(acceptInvitedHangOutRequest.getInviteHangOutId());

        hangOutService.acceptInvitedHangOut(acceptInvitedHangOutRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }
}

















