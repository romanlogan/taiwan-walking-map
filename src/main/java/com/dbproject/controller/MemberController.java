package com.dbproject.controller;

import com.dbproject.dto.MyProfileDto;
import com.dbproject.dto.RegisterFormDto;
import com.dbproject.dto.UpdateProfileDto;
import com.dbproject.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;


    @GetMapping("/registration")
    public String provideMemberForm(Model model) {

        model.addAttribute("registerFormDto", new RegisterFormDto());
        return "/member/registerForm";
    }

    @PostMapping("/registration")
    public String newMember(@Valid RegisterFormDto registerFormDto,
                            BindingResult bindingResult,
                            Model model) {

        if (bindingResult.hasErrors()) {
            return "/member/registerForm";
        }

        memberService.saveMember(registerFormDto, passwordEncoder);

        return "redirect:/";
    }


    @GetMapping(value = "/login")
    public String loginMember() {

        return "/member/loginForm";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인해주세요");
        return "/member/loginForm";
    }

    @GetMapping(value = "/profile")
    public String memberProfile(Principal principal, Model model) {

        String email = principal.getName();
        MyProfileDto myProfileDto = memberService.findMe(email);

        model.addAttribute("user", myProfileDto);


        return "/member/myProfile";
    }

    @PutMapping("/update")
    public ResponseEntity<Long> updateProfile(@RequestBody UpdateProfileDto updateProfileDto,
                                              Principal principal,
                                              Model model) {

        //update 와 조회 를 분리
        memberService.updateProfile(principal.getName(),updateProfileDto);
        MyProfileDto myProfileDto = memberService.findMe(principal.getName());

        model.addAttribute("user", myProfileDto);

        return new ResponseEntity<Long>(myProfileDto.getId(), HttpStatus.OK);

    }
}
