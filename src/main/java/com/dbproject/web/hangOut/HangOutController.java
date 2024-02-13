package com.dbproject.web.hangOut;

import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOut;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutService;
import com.dbproject.api.myPage.hangOut.inviteHangOut.InviteHangOutRequest;
import com.dbproject.api.myPage.hangOut.inviteHangOut.dto.*;
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
@RequestMapping("/hangOut")
public class HangOutController {

    private final InviteHangOutService hangOutService;

//    마이페이지 에서 즐겨찾기를 사용해서 HangOut 초대
//    @PostMapping("/inviteHangOut")
    @PostMapping("/inviteFromFriendList")
    public ResponseEntity inviteFromFriendList(@RequestBody InviteHangOutRequest inviteHangOutRequest,
                                        Principal principal) {

        Long id = hangOutService.inviteHangOut(inviteHangOutRequest, principal.getName());

        return new ResponseEntity(1L, HttpStatus.OK);
    }

    //    마이페이지 에서 즐겨찾기를 사용해서 HangOut 초대
    @PostMapping("/inviteFromLocationPage")
    public ResponseEntity inviteFromLocationPage(@RequestBody InviteHangOutFromLocRequest inviteHangOutFromLocRequest) {

        hangOutService.inviteFromLocationPage(inviteHangOutFromLocRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }


    @GetMapping("/invitedList")
    public String getInvitedHangOutList(@RequestParam(name = "inviteHangOutId") Optional<Long> inviteHangOutId,
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

    @DeleteMapping("/rejectInvitedHangOut")
    public ResponseEntity rejectInvitedHangOut(@RequestBody RejectInvitedHangOutRequest rejectInvitedHangOutRequest) {

        hangOutService.rejectInvitedHangOut(rejectInvitedHangOutRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }
}

















