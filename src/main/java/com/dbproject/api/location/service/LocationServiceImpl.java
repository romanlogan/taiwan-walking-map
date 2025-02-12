package com.dbproject.api.location.service;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.CommentDto;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.comment.repository.CommentRepository;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.friend.Friend;
import com.dbproject.api.friend.dto.FriendDto;
import com.dbproject.api.friend.repository.FriendRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.location.dto.*;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.memberImg.MemberImg;
import com.dbproject.api.member.memberImg.MemberImgRepository;
import com.dbproject.exception.LocationNotExistException;
import com.dbproject.exception.RegionSearchConditionNotValidException;
import com.dbproject.exception.TownSearchConditionNotValidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final MemberImgRepository memberImgRepository;
    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;



    // ----------------비 로그인 유저가 장소 디테일을 볼때-----------------
    public LocationDtlResponse getLocationDtl(String locationId) {

        Location location = locationRepository.findByLocationId(locationId);

        checkLocationExists(location);

        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        getCommentList(locationId, locationDtlResponse);


        return locationDtlResponse;
    }

    private static void checkLocationExists(Location location) {
        if (location == null) {
            throw new LocationNotExistException("Location 이 존재하지 않습니다.");
        }
    }

    // ----------------로그인 유저가 장소 디테일을 볼때-----------------
    public LocationDtlResponse getLocationDtlWithAuthUser(String locationId, String email) {

        Location location = locationRepository.findByLocationId(locationId);
        checkLocationExists(location);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        //로그인 유저는 이 장소가 favorite 에 등록 되어 있는지 확인
        checkSavedFavoriteLocation(location, locationDtlResponse, email);

        // 댓글 가져오기
        getCommentList(locationId, locationDtlResponse);

        // 로그인 한 유저의 친구 목록을 가져오기 , getCommentList 로직에서 member 관련 로직이랑 조금 겹치는 부분이 존재
        getFriendList(email, locationDtlResponse);

        return locationDtlResponse;
    }

    private void getFriendList(String email, LocationDtlResponse locationDtlResponse) {
        List<Friend> friendList = friendRepository.getFriendList(email);

        List<FriendDto> friendDtoList = new ArrayList<>();
        for (Friend friend : friendList) {

            Optional<MemberImg> optionalMemberImg = memberImgRepository.findByMemberEmail(friend.getNewFriend().getEmail());
            FriendDto friendDto = FriendDto.from(friend, optionalMemberImg);
            friendDtoList.add(friendDto);
        }
        locationDtlResponse.setFriendDtoList(friendDtoList);
    }

    private void getCommentList(String locationId, LocationDtlResponse locationDtlResponse) {

        List<Comment> commentList = commentRepository.findByLocationId(locationId);

        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {

//            여기 로직을 commentDTo 안으로 넣어야함 (FriendDto 참고 )
            String email = comment.getMember().getEmail();
            Optional<MemberImg> optionalMemberImg = memberImgRepository.findByMemberEmail(email);
            String imgUrl;
            if (optionalMemberImg.isPresent()) {
                MemberImg memberImg = optionalMemberImg.get();
                imgUrl = memberImg.getImgUrl();
            } else {
                imgUrl = "/img/noImg.jpg";
            }

            CommentDto commentDto = CommentDto.from(comment, imgUrl);
            commentDtoList.add(commentDto);
        }

        locationDtlResponse.setCommentDtoList(commentDtoList);
//        locationDtlResponse.setCommentList(commentList);
    }

    private void checkSavedFavoriteLocation(Location location, LocationDtlResponse locationDtlResponse, String email) {

//        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId(location.getLocationId());
        FavoriteLocation favoriteLocation = favoriteRepository.findDuplicateFavoriteLocation(location.getLocationId(), email);

        if (favoriteLocation == null) {
            // 기본값이 false 니까 이건 안쓰는게 맞을까 ?
            locationDtlResponse.setNotSaved();
        } else {
            locationDtlResponse.setSaved();
        }
    }

    public RecommendLocationListResponse getRecommendLocationList(RecommendLocationListRequest request, Pageable pageable) {

        checkRegionSearchConditionIsValid(request);
        checkTownSearchConditionIsValid(request);

        Page<RecommendLocationDto> locationList = locationRepository.getLocationPageByCity(request, pageable);
        SearchRequestConditionDto searchConditionDto = SearchRequestConditionDto.create(request.getSearchArrival(), request.getSearchQuery(), request.getSearchTown());
        List<String> townList = getTownListFrom(request.getSearchArrival());

        return RecommendLocationListResponse.create(locationList, searchConditionDto,townList);
    }

    @Override
    public List<String> getTownListFrom(String region) {

        return locationRepository.findTownListByRegion(region);
    }

    private void checkRegionSearchConditionIsValid(RecommendLocationListRequest request) {
        List<Location> locationList = locationRepository.findByRegion(
                request.getSearchArrival());

        if (locationList.isEmpty()) {
            throw new RegionSearchConditionNotValidException("Region 검색어가 올바르지 않습니다.");
        }
    }

    private void checkTownSearchConditionIsValid(RecommendLocationListRequest request) {
        List<Location> townList = locationRepository.findByTownLikeAndRegion(
                request.getSearchTown(), request.getSearchArrival());

        if (townList.isEmpty()) {
            throw new TownSearchConditionNotValidException("Town 검색어가 올바르지 않습니다.");
        }
    }
}
