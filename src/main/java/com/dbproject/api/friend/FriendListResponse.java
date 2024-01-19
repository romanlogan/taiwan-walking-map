package com.dbproject.api.friend;

import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteLocationDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FriendListResponse {

    private Page<FriendDto> friendListPages;

    private List<FavoriteLocationDto> favoriteLocationList = new ArrayList<>();

    @Builder
    private FriendListResponse(Page<FriendDto> friendListPages, List<FavoriteLocationDto> favoriteLocationList) {
        this.friendListPages = friendListPages;
        this.favoriteLocationList = favoriteLocationList;
    }

    public static FriendListResponse createFriendListResponse(Page<FriendDto> friendListPages, List<FavoriteLocationDto> favoriteLocationList) {

        return FriendListResponse.builder()
                .friendListPages(friendListPages)
                .favoriteLocationList(favoriteLocationList)
                .build();
    }

}
