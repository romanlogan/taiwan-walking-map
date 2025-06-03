package com.dbproject.api.member;

import com.dbproject.api.baseEntity.BaseEntity;
import com.dbproject.api.friend.Friend;
import com.dbproject.api.member.dto.RegisterFormDto;
import com.dbproject.api.invitePlan.invitePlanMember.InvitePlanMember;
import com.dbproject.api.planMember.PlanMember;
import com.dbproject.constant.Role;
import com.dbproject.api.favorite.FavoriteLocation;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "member")
@Getter
@Setter
public class Member extends BaseEntity {

//    @Id
//    @Column(name="member_id")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @Column(name = "member_email")
    private String email;

    private String name;

    private String nickName;

    private String password;

    private String address;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String gender;

    private Boolean acceptReceiveAdvertising;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member")
    private List<FavoriteLocation> favoriteLocation;

    @OneToMany(mappedBy = "newFriend")
    private List<Friend> friends;

    @OneToMany(mappedBy = "member")
    private List<InvitePlanMember> invitedPlanList;

    @OneToMany(mappedBy = "member")
    private List<PlanMember> planList;

    public Member() {
    }

    @Builder
    public Member(String email, String name, String password, String address, String nickName, String phoneNumber, LocalDate dateOfBirth, String gender, Boolean acceptReceiveAdvertising, Role role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.acceptReceiveAdvertising = acceptReceiveAdvertising;
        this.role = role;
    }

    public static Member createMember(RegisterFormDto registerFormDto,
                                      PasswordEncoder passwordEncoder) {

        return Member.builder()
                .email(registerFormDto.getEmail())
                .name(registerFormDto.getName())
                .password(passwordEncoder.encode(registerFormDto.getPassword()))
                .address(registerFormDto.getAddress())
                .phoneNumber(registerFormDto.getPhoneNumber())
                .dateOfBirth(registerFormDto.getDateOfBirth())
                .gender(registerFormDto.getGender())
                .acceptReceiveAdvertising(registerFormDto.getAcceptReceiveAdvertising())
                .role(Role.USER)
                .build();
    }

    public void addFriend(Friend friend) {
        this.friends.add(friend);
    }

}










