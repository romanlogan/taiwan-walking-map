package com.dbproject.api.location;

import com.dbproject.web.location.LocationDtlResponse;
import com.dbproject.web.location.RecLocationListRequest;
import com.dbproject.web.location.RecLocationListResponse;
import com.dbproject.api.comment.Comment;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.comment.CommentRepository;
import com.dbproject.api.favorite.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        checkSavedFavoriteLocation(location,locationDtlResponse,email);

        List<Comment> commentList = commentRepository.findByLocationId(locationId);
        locationDtlResponse.setCommentList(commentList);

        return locationDtlResponse;
    }



    private void checkSavedFavoriteLocation(Location location, LocationDtlResponse locationDtlResponse, String email) {

//        FavoriteLocation favoriteLocation = favoriteRepository.findByLocationId(location.getLocationId());
        FavoriteLocation favoriteLocation = favoriteRepository.duplicateFavoriteLocation(location.getLocationId(), email);

        if (favoriteLocation == null) {
            // 기본값이 false 니까 이건 안쓰는게 맞을까 ?
            locationDtlResponse.setNotSaved();
        }else{
            locationDtlResponse.setSaved();
        }

    }

    public Page<RecLocationListResponse> getLocationPageByCity(RecLocationListRequest request, Pageable pageable) {

        Page<RecLocationListResponse> locationList = locationRepository.getLocationPageByCity(request, pageable);

        return locationList;
    }
}
