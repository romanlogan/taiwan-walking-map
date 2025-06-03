package com.dbproject.api.member.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class RegisterFormDto {

    @NotBlank(message = "電子郵件 - 必要")
    @Email(message = "請輸入電子郵件形式")
    private String email;

    @NotBlank(message = "名字 - 必要")
    private String name;

    @NotBlank(message = "密碼 - 必要")
    @Length(min=8, max=16, message = "密碼必須 8字以上, 16字一下")
    private String password;

    @NotEmpty(message = "地址 - 必要")
    private String address;

    @NotBlank(message = "電話號碼 - 必要")
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "生日 - 必要")
    private LocalDate dateOfBirth;

    @NotBlank(message = "性別 - 必要")
    private String gender;

    @NotNull(message = "廣告 - 必要")
    private Boolean acceptReceiveAdvertising;

    @Override
    public String toString() {
        return "RegisterFormDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender=" + gender +
                ", acceptReceiveAdvertising=" + acceptReceiveAdvertising +
                '}';
    }

    public RegisterFormDto() {
    }

    @Builder
    public RegisterFormDto(String email, String name, String password, String address, String phoneNumber, LocalDate dateOfBirth, String gender, Boolean acceptReceiveAdvertising) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.acceptReceiveAdvertising = acceptReceiveAdvertising;
    }

    public static RegisterFormDto createForTest(String name, String address, String email, String password) {

        return RegisterFormDto.builder()
                .name(name)
                .address(address)
                .email(email)
                .password(password)
                .build();

    }

}
