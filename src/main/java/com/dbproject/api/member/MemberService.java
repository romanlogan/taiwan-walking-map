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

        Member member;
        checkDuplicateMember(registerFormDto);

        member = Member.createMember(registerFormDto, passwordEncoder);

        memberRepository.save(member);
    }

    private void checkDuplicateMember(RegisterFormDto registerFormDto) {

        Member member = memberRepository.findByEmail(registerFormDto.getEmail());

        if (member != null) {
            throw new DuplicateMemberException("동일한 아이디가 존재 합니다.");
//            throw new IllegalArgumentException("동일한 아이디가 존재 합니다.");
        }
    }

    public MyProfileDto findMe(String name) {

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

//    @Transactional
    public void updateProfile(String email, UpdateProfileRequest updateProfileDto) {

        Member member = memberRepository.findByEmail(email);

        if (updateProfileDto.getAddress().equals(member.getAddress())) {
            throw new DuplicateUpdateMemberAddressException("이전 주소와 같습니다.");
        } else if (updateProfileDto.getName().equals(member.getName())) {
            throw new DuplicateUpdateMemberNameException("이전 이름과 같습니다.");
        }

        member.setName(updateProfileDto.getName());
        member.setAddress(updateProfileDto.getAddress());

    }

    public Long deleteMember(String email) {

//        에러 코드 바뀐거 확인 완료 -> member 와 연관된 다른 데이터도 삭제 필요
        commentRepository.deleteByMemberEmail(email);
//    양방향이 아닌 관계는 수동으로 삭제 해야 하는 건가 ?
        favoriteRepository.deleteByMemberEmail(email);
        friendRepository.deleteByEmail(email);
        friendRequestRepository.deleteByEmail(email);
        inviteHangOutRepository.deleteByEmail(email);
        hangOutRepository.deleteByEmail(email);
        memberImgRepository.deleteByMemberEmail(email);

        Member member = memberRepository.findByEmail(email);
        memberRepository.deleteByEmail(email);

        return 1L;
    }



}




















