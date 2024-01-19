package com.dbproject.api.friend;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendDto {

    private Long id;

    private String friendEmail;

    private String friendName;

    private String friendAddress;


    @QueryProjection
    public FriendDto(Long id, String friendEmail, String friendName, String friendAddress) {
        this.id = id;
        this.friendEmail = friendEmail;
        this.friendName = friendName;
        this.friendAddress = friendAddress;
    }
}
