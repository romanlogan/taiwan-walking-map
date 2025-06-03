package com.dbproject.api.friend.friendRequest.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class RequestFriendListResponse {

    Page<RequestFriendListDto> requestFriendListDtos;
}
