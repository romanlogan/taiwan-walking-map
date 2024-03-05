package com.dbproject.api.friend.dto;

import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FriendListResponse {

    private List<FriendDto> friendDtoList;

    private List<FavoriteLocationDto> favoriteLocationList = new ArrayList<>();

    @Builder
    public FriendListResponse(List<FriendDto> friendDtoList, List<FavoriteLocationDto> favoriteLocationList) {
        this.friendDtoList = friendDtoList;
        this.favoriteLocationList = favoriteLocationList;
    }

    public static FriendListResponse createFriendListResponse(List<FriendDto> friendDtoList, List<FavoriteLocationDto> favoriteLocationList) {

        return FriendListResponse.builder()
                .friendDtoList(friendDtoList)
                .favoriteLocationList(favoriteLocationList)
                .build();
    }

}
