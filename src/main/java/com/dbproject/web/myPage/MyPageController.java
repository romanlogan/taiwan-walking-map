package com.dbproject.web.myPage;

import com.dbproject.api.member.MemberService;
import com.dbproject.api.member.memberImg.MemberImgService;
import com.dbproject.api.member.dto.MyProfileDto;
import com.dbproject.api.member.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/myPage")
public class MyPageController {

    private final MemberService memberService;
    private final MemberImgService memberImgService;


    @GetMapping(value = "/profile")
    public String memberProfile(Principal principal, Model model) {

        MyProfileDto myProfileDto = memberService.getMyProfile(principal.getName());
        model.addAttribute("user", myProfileDto);

        return "myPage/myProfile";
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updateProfile(@Valid @RequestBody UpdateProfileRequest updateProfileRequest,
                                              Principal principal,
                                              Model model) {
        //update 와 조회 를 분리
        memberService.updateProfile(principal.getName(),updateProfileRequest);

        MyProfileDto myProfileDto = memberService.getMyProfile(principal.getName());

        model.addAttribute("user", myProfileDto);

        return new ResponseEntity<Long>(1L, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Long> deleteMember(Principal principal) {

        String email = principal.getName();
        memberService.deleteMember(email);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/updateMemberImg")
    public String updateMemberImg(@RequestParam("memberImgFile") MultipartFile memberImgFile,
                                          Principal principal) throws Exception{

        memberImgService.updateMemberImg(memberImgFile, principal.getName());

//        return "redirect:/myPage/profile";
        return "redirect:/";

    }

}

