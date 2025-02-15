package com.dbproject.api.location.service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
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

    public LocationDtlResponse getLocationDtl(String locationId, String name) {

        Pageable pageable = PageRequest.of(0, 5);

//      locaion query
        Location location = getLocationById(locationId);
        checkLocationExists(location);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.create(location);

        setCommentList(locationId, locationDtlResponse,pageable);

//        로그인 유저
        if (name != null) {
            //로그인 유저는 이 장소가 favorite 에 등록 되어 있는지 확인
            checkSavedFavoriteLocation(location, locationDtlResponse, name);
            // 로그인 한 유저의 친구 목록을 가져오기 , getCommentList 로직에서 member 관련 로직이랑 조금 겹치는 부분이 존재
            getFriendList(name, locationDtlResponse);
        }

        return locationDtlResponse;
    }

    private Location getLocationById(String locationId) {
        Location location = locationRepository.findByLocationId(locationId);
        checkLocationExists(location);
        return location;
    }

    private static void checkLocationExists(Location location) {
        if (location == null) {
            throw new LocationNotExistException("Location 이 존재하지 않습니다.");
        }
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

    private void setCommentList(String locationId, LocationDtlResponse locationDtlResponse,Pageable pageable) {

        List<CommentDto> commentDtoList = commentRepository.findByLocationIdWithJoin(locationId, pageable);

        for (CommentDto comment : commentDtoList) {
            if (comment.getImgUrl() == null) {
                comment.setImgUrl("/img/noImg.jpg");
            }
        }

        locationDtlResponse.setCommentDtoList(commentDtoList);
    }

    private void checkSavedFavoriteLocation(Location location, LocationDtlResponse locationDtlResponse, String email) {

        FavoriteLocation favoriteLocation = favoriteRepository.findDuplicateFavoriteLocation(location.getLocationId(), email);

        if (favoriteLocation == null) {
            // 기본값이 false 니까 이건 안쓰는게 맞을까 ?
            locationDtlResponse.setNotSaved();  //저장되지 않았음
        } else {
            locationDtlResponse.setSaved();     //이미 저장한 적이 있는 즐겨찾기
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
