package com.dbproject.api.location;

import com.dbproject.api.comment.Comment;
import com.dbproject.api.comment.dto.CommentDto;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.comment.CommentRepository;
import com.dbproject.api.favorite.FavoriteRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.api.member.memberImg.MemberImg;
import com.dbproject.api.member.memberImg.MemberImgRepository;
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
public class LocationService {

    private final LocationRepository locationRepository;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final MemberImgRepository memberImgRepository;



// 비 로그인 유저가 장소 디테일을 볼때
    public LocationDtlResponse getLocationDtl(String locationId) {

        Location location = locationRepository.findByLocationId(locationId);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        getCommentList(locationId, locationDtlResponse);


        return locationDtlResponse;
    }

    private void getCommentList(String locationId, LocationDtlResponse locationDtlResponse) {

        List<Comment> commentList = commentRepository.findByLocationId(locationId);

        List<CommentDto> commentDtoList = new ArrayList<>();
        for (Comment comment : commentList) {

            String email = comment.getMember().getEmail();
            Optional<MemberImg> optionalMemberImg = memberImgRepository.findByMemberEmail(email);
            String imgUrl;
            if (optionalMemberImg.isPresent()) {
                MemberImg memberImg = optionalMemberImg.get();
                imgUrl = memberImg.getImgUrl();
            }else{
                imgUrl = "/img/noImg.jpg";
            }

            CommentDto commentDto = CommentDto.from(comment, imgUrl);
            commentDtoList.add(commentDto);
        }

        locationDtlResponse.setCommentDtoList(commentDtoList);
//        locationDtlResponse.setCommentList(commentList);
    }

    // 로그인 유저가 장소 디테일을 볼때
    public LocationDtlResponse getLocationDtlWithAuthUser(String locationId,String email) {

        Location location = locationRepository.findByLocationId(locationId);
        LocationDtlResponse locationDtlResponse = LocationDtlResponse.of(location);

        //로그인 유저는 이 장소가 favorite 에 등록 되어 있는지 확인
        checkSavedFavoriteLocation(location,locationDtlResponse,email);

        getCommentList(locationId, locationDtlResponse);

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
