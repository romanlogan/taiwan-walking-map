package com.dbproject.entity;

import com.dbproject.constant.Role;
import com.dbproject.dto.RegisterFormDto;
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

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

//    다대다 단방향으로 외래키는 연결 테이블이나 여기 테이블에 두기
    @OneToMany(mappedBy = "member",fetch = FetchType.LAZY)
    private List<FavoriteLocation> favoriteLocation;



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










