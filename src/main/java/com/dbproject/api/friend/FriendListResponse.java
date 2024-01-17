package com.dbproject.api.friend;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FriendListResponse {

    private Long id;

    private String requesterEmail;

    private String respondentEmail;

    private String name;

    private String address;


    @QueryProjection
    public FriendListResponse(Long id, String requesterEmail, String respondentEmail, String name, String address) {
        this.id = id;
        this.requesterEmail = requesterEmail;
        this.respondentEmail = respondentEmail;
        this.name = name;
        this.address = address;
    }
}
