package com.dbproject.api.member;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.friend.FriendRequest;
import com.dbproject.constant.Role;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.web.member.RegisterFormDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @Column(name="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true,
            name = "member_email")
    private String email;

    private String password;

    private String address;

    //전화번호

    @Enumerated(EnumType.STRING)
    private Role role;


    @OneToMany(mappedBy = "member")
    private List<FavoriteLocation> favoriteLocation;

//    @OneToMany
//    private List<Member> friend;



//    //    요청한거
////    @Column(name="request_friend")
//    @OneToMany(mappedBy = "requester")
//    private List<FriendRequest> requestFriend;
//
//    //    요청 받은거
////    @Column(name="be_requested_friend")
//    @OneToMany(mappedBy = "respondent")
//    private List<FriendRequest> beRequestedFriend;



    @Builder
    public Member(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public static Member createMember(RegisterFormDto registerFormDto,
                                      PasswordEncoder passwordEncoder) {

        return Member.builder()
                .name(registerFormDto.getName())
                .email(registerFormDto.getEmail())
                .password(passwordEncoder.encode(registerFormDto.getPassword()))
                .address(registerFormDto.getAddress())
                .role(Role.USER)
                .build();
    }


}










