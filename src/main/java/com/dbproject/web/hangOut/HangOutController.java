package com.dbproject.web.hangOut;

import com.dbproject.api.hangOut.inviteHangOut.service.InviteHangOutService;
import com.dbproject.api.hangOut.inviteHangOut.service.InviteHangOutServiceImpl;
import com.dbproject.api.hangOut.inviteHangOut.dto.InviteHangOutRequest;
import com.dbproject.api.hangOut.inviteHangOut.dto.AcceptInvitedHangOutRequest;
import com.dbproject.api.hangOut.inviteHangOut.dto.InviteHangOutFromLocRequest;
import com.dbproject.api.hangOut.inviteHangOut.dto.InvitedHangOutResponse;
import com.dbproject.api.hangOut.inviteHangOut.dto.RejectInvitedHangOutRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/hangOut")
public class HangOutController {

    private final InviteHangOutService hangOutService;

//    마이페이지 에서 즐겨찾기를 사용해서 HangOut 초대
//    @PostMapping("/inviteHangOut")
    @PostMapping("/inviteFromFriendList")
    public ResponseEntity inviteFromFriendList(@Valid @RequestBody InviteHangOutRequest inviteHangOutRequest,
                                        Principal principal) {

        Long id = hangOutService.inviteHangOut(inviteHangOutRequest, principal.getName());

        return new ResponseEntity(1L, HttpStatus.OK);
    }

    //    장소 정보 페이지 에서 HangOut 초대
    @PostMapping("/inviteFromLocationPage")
    public ResponseEntity inviteFromLocationPage(@Valid @RequestBody InviteHangOutFromLocRequest inviteHangOutFromLocRequest,
                                                 Principal principal) {

        hangOutService.inviteFromLocationPage(inviteHangOutFromLocRequest,principal.getName());

        return new ResponseEntity(1L, HttpStatus.OK);
    }


    @GetMapping("/invitedList")
    public String getInvitedHangOutList(@RequestParam(name = "inviteHangOutId") Optional<Long> inviteHangOutId,
                                       Principal principal, Model model) {


        InvitedHangOutResponse invitedHangOutResponse = hangOutService.getInvitedHangOutList(principal.getName(), inviteHangOutId);

        model.addAttribute("invitedHangOutResponse", invitedHangOutResponse);
        return "myPage/inviteHangOutList";
    }

    @PostMapping("/acceptInvitedHangOut")
    public ResponseEntity acceptInvitedHangOut(@Valid @RequestBody AcceptInvitedHangOutRequest acceptInvitedHangOutRequest) {

        hangOutService.acceptInvitedHangOut(acceptInvitedHangOutRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }

    @DeleteMapping("/rejectInvitedHangOut")
    public ResponseEntity rejectInvitedHangOut(@Valid @RequestBody RejectInvitedHangOutRequest rejectInvitedHangOutRequest) {

        hangOutService.rejectInvitedHangOut(rejectInvitedHangOutRequest);

        return new ResponseEntity(1L, HttpStatus.OK);
    }
}

















