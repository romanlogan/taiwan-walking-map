package com.dbproject.web.member;

import com.dbproject.api.member.MemberService;
import com.dbproject.api.member.dto.RegisterFormDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    private final PasswordEncoder passwordEncoder;

//    @GetMapping("/")
//    public String profileHome() {
//
//        return "redirect:/members/profile";
//    }

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
            log.info("error={} ", bindingResult);
            return "/member/registerForm";
        }

        System.out.println(registerFormDto.toString());
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


}
