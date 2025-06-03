package com.dbproject.api.member;

import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.friend.repository.FriendRepository;
import com.dbproject.api.friend.friendRequest.repository.FriendRequestRepository;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.member.memberImg.MemberImg;
import com.dbproject.api.member.memberImg.MemberImgRepository;
import com.dbproject.api.hangOut.hangOut.repository.HangOutRepository;
import com.dbproject.api.hangOut.inviteHangOut.repository.InviteHangOutRepository;
import com.dbproject.exception.DuplicateMemberException;
import com.dbproject.exception.DuplicateUpdateMemberAddressException;
import com.dbproject.exception.DuplicateUpdateMemberNameException;
import com.dbproject.api.member.dto.MyProfileDto;
import com.dbproject.api.member.dto.UpdateProfileRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberImgRepository memberImgRepository;
    private final CommentRepository commentRepository;
    private final FavoriteRepository favoriteRepository;
    private final FriendRepository friendRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final HangOutRepository hangOutRepository;
    private final InviteHangOutRepository inviteHangOutRepository;

    public void saveMember(RegisterFormDto registerFormDto, PasswordEncoder passwordEncoder) {

        checkDuplicateMember(registerFormDto);
        memberRepository.save(Member.createMember(registerFormDto, passwordEncoder));
    }

    private void checkDuplicateMember(RegisterFormDto registerFormDto) {

        Member member = memberRepository.findByEmail(registerFormDto.getEmail());

        if (member != null) {
            throw new DuplicateMemberException("An identical ID already exists.");
        }
    }

    @Transactional(readOnly = true)
    public MyProfileDto getMyProfile(String name) {

        Member member = memberRepository.findByEmail(name);
        Optional<MemberImg> memberImg = memberImgRepository.findByMemberEmail(name);
        return MyProfileDto.from(member,memberImg);
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

    public void updateProfile(String email, UpdateProfileRequest updateProfileDto) {

        Member member = memberRepository.findByEmail(email);

        if (updateProfileDto.getAddress().equals(member.getAddress())) {
            throw new DuplicateUpdateMemberAddressException("Same as previous address.");
        } else if (updateProfileDto.getName().equals(member.getName())) {
            throw new DuplicateUpdateMemberNameException("Same as previous name.");
        }

        member.setName(updateProfileDto.getName());
        member.setAddress(updateProfileDto.getAddress());
    }

    public void deleteMember(String email) {

        commentRepository.deleteByMemberEmail(email);
        favoriteRepository.deleteByMemberEmail(email);
        friendRepository.deleteByEmail(email);
        friendRequestRepository.deleteByEmail(email);
        inviteHangOutRepository.deleteByEmail(email);
        hangOutRepository.deleteByEmail(email);
        memberImgRepository.deleteByMemberEmail(email);

        Member member = memberRepository.findByEmail(email);
        memberRepository.deleteByEmail(email);
    }
}




















