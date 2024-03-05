package com.dbproject.api.member.dto;


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

//    private String nickName;

    @NotBlank(message = "電話號碼 - 必要")
    private String phoneNumber;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "電話號碼 - 必要")
    private LocalDate dateOfBirth;

    @Range(min = 0, max = 2 ,message = "形式錯誤（性別）")
    @NotNull(message = "電話號碼 - 必要")
    private Integer gender;

    @NotNull(message = "電話號碼 - 必要")
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
}
