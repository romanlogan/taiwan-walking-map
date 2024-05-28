package com.dbproject.api.favorite.service;

import com.dbproject.api.favorite.dto.*;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import com.dbproject.exception.FavoriteLocationNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;


    public Long addFavoriteList(AddFavoriteLocationRequest addFavoriteLocationRequest, String email) throws DuplicateFavoriteLocationException{

        checkDuplicateFavoriteLocation(addFavoriteLocationRequest.getLocationId(), email);

        FavoriteLocation favoriteLocation = createAndSaveFavoriteLocationFrom(addFavoriteLocationRequest, email);

        return favoriteLocation.getId();
    }

    private FavoriteLocation createAndSaveFavoriteLocationFrom(AddFavoriteLocationRequest addFavoriteLocationRequest, String email) {

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(addFavoriteLocationRequest.getLocationId());
        return saveFavoriteLocation(location, FavoriteLocation.of(member, location, addFavoriteLocationRequest.getMemo()));
    }

    private FavoriteLocation saveFavoriteLocation(Location location, FavoriteLocation favoriteLocation) {

        FavoriteLocation savedFavoriteLocation = favoriteRepository.save(favoriteLocation);
        location.increaseFavoriteCount();       //increase 는 save 한 뒤에 하기,
        return savedFavoriteLocation;
    }

    private void checkDuplicateFavoriteLocation(String locationId, String email) {

        FavoriteLocation favoriteLocation = favoriteRepository.findDuplicateFavoriteLocation(locationId, email);

        if (favoriteLocation != null) {
            throw new DuplicateFavoriteLocationException("이미 등록된 장소 입니다.");
        }
    }

    public FavoriteLocationListResponse getFavoriteLocationList(Pageable pageable, String email) {

        Page<FavoriteLocationDto> favoriteLocationPage = favoriteRepository.getFavoriteLocationListPage(pageable, email);

        return new FavoriteLocationListResponse(favoriteLocationPage);
    }

    public void deleteFavoriteLocation(DeleteFavoriteLocationRequest request){

        FavoriteLocation favoriteLocation = findFavoriteLocationBy(request.getFavoriteLocationId());
        favoriteLocation.decreaseLocationFavoriteCount();
        favoriteRepository.delete(favoriteLocation);

    }

    private FavoriteLocation findFavoriteLocationBy(Integer id) {
        Optional<FavoriteLocation> optionalFavoriteLocation = favoriteRepository.findById(Long.valueOf(id));
        checkFavoriteLocationExist(optionalFavoriteLocation);

        return optionalFavoriteLocation.get();
    }

    private static void checkFavoriteLocationExist(Optional<FavoriteLocation> optionalFavoriteLocation) {

        if (optionalFavoriteLocation.isEmpty()) {
            throw new FavoriteLocationNotExistException("즐겨찾기 장소가 존재하지 않습니다.");
        }
    }

    public Integer getMaxPage(Integer size) {

        Integer totalCount = Math.toIntExact(favoriteRepository.count());

        //page 는 0부터 시작하므로 +1 안해도 됨
        return (totalCount / size) ;
    }

    public Long updateMemo(UpdateMemoRequest updateMemoRequest) {

        FavoriteLocation favoriteLocation = findFavoriteLocationBy(updateMemoRequest.getFavoriteLocationId());

        favoriteLocation.updateMemo(updateMemoRequest.getMemo());

        return favoriteLocation.getId();
    }

}
