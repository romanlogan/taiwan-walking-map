package com.dbproject.api.myPage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class UpdateProfileRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    public UpdateProfileRequest(String name, String address) {
        this.name = name;
        this.address = address;
    }
}
