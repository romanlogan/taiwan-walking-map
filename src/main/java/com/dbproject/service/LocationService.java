package com.dbproject.service;

import com.dbproject.dto.LocationDtlResponse;
import com.dbproject.dto.SearchByCityDto;
import com.dbproject.entity.Comment;
import com.dbproject.entity.FavoriteLocation;
import com.dbproject.entity.Location;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.repository.CommentRepository;
import com.dbproject.repository.FavoriteRepository;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;



// 비 로그인 유저가 장소 디테일을 볼때
    public LocationDtlResponse getLocationDtl(String locationId) {

        Location location = locationRepository.findByLocationId(locationId);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        List<Comment> commentList = commentRepository.findByLocationId(locationId);
        locationDtlResponse.setCommentList(commentList);


        return locationDtlResponse;
    }

    // 로그인 유저가 장소 디테일을 볼때
    public LocationDtlResponse getLocationDtlWithAuthUser(String locationId,String email) {

        Location location = locationRepository.findByLocationId(locationId);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        //로그인 유저는 이 장소가 favorite 에 등록 되어 있는지 확인
        checkSavedFavoriteLocation(location,locationDtlResponse);

        List<Comment> commentList = commentRepository.findByLocationId(locationId);
        locationDtlResponse.setCommentList(commentList);

        return locationDtlResponse;
    }



    private void checkSavedFavoriteLocation(Location location, LocationDtlResponse locationDtlResponse) {

        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId(location.getLocationId());

        if (favoriteLocation == null) {
            // 기본값이 false 니까 이건 안쓰는게 맞을까 ?
            locationDtlResponse.setNotSaved();
        }else{
            locationDtlResponse.setSaved();
        }

    }

    public Page<Location> getLocationPageByCity(SearchByCityDto searchByCityDto, Pageable pageable) {

        Page<Location> locationList = locationRepository.getLocationPageByCity(searchByCityDto.getArriveCity(), pageable);

        return locationList;
    }
}
