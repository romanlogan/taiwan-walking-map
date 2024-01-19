package com.dbproject.api.friend.friendRequest;


import com.dbproject.api.friend.friendRequest.RequestFriendListDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class RequestFriendListResponse {

    Page<RequestFriendListDto> requestFriendListDtos;

}
