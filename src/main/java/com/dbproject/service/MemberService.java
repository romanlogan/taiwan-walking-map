package com.dbproject.service;

import com.dbproject.dto.RegisterFormDto;
import com.dbproject.entity.Member;
import com.dbproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public void saveMember(RegisterFormDto registerFormDto, PasswordEncoder passwordEncoder) {
        Member member;
        checkDuplicateMember(registerFormDto);

        member = Member.createMember(registerFormDto, passwordEncoder);

        memberRepository.save(member);
    }

    private void checkDuplicateMember(RegisterFormDto registerFormDto) {

        Member member = memberRepository.findByEmail(registerFormDto.getEmail());

        if (member != null) {
//            throw new DuplicateMemberException("동일한 아이디가 존재 합니다.");
            throw new IllegalArgumentException("동일한 아이디가 존재 합니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{

        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }


}




















