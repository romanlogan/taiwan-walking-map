package com.dbproject.api.myPage.hangOut;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class InviteHangOutRequest {

    @NotNull
    private Integer favoriteLocationId;

    @NotNull
    private LocalDateTime departDateTime;

    private String message;

    @NotEmpty
    private String friendEmail;

}
