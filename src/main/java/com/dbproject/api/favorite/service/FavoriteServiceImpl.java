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

//        Each location can only be saved to favorites once.
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
        location.increaseFavoriteCount();
        return savedFavoriteLocation;
    }

    private void checkDuplicateFavoriteLocation(String locationId, String email) {

        FavoriteLocation favoriteLocation = favoriteRepository.findDuplicateFavoriteLocation(locationId, email);

        if (favoriteLocation != null) {
            throw new DuplicateFavoriteLocationException("This location is already registered in favorite list");
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
            throw new FavoriteLocationNotExistException("favorite location does not exist.");
        }
    }

//    return max page number
    public Integer getMaxPage(Integer pageSize, String email) {

//        get total favorite count of user
        Integer totalCount = Math.toIntExact(favoriteRepository.getTotalFavoriteCount(email));

        // calculate maximum page of user's favorite list
        return (totalCount / pageSize) ;
    }

    public Long updateMemo(UpdateMemoRequest updateMemoRequest) {

        FavoriteLocation favoriteLocation = findFavoriteLocationBy(updateMemoRequest.getFavoriteLocationId());

        favoriteLocation.updateMemo(updateMemoRequest.getMemo());

        return favoriteLocation.getId();
    }

}
