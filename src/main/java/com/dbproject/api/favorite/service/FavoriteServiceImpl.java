package com.dbproject.api.favorite.service;

import com.dbproject.api.favorite.dto.AddFavoriteLocationRequest;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.repository.FavoriteRepository;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.api.favorite.dto.UpdateMemoRequest;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.api.location.repository.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService{

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;


    public Long addFavoriteList(AddFavoriteLocationRequest addFavoriteLocationRequest, String email) throws DuplicateFavoriteLocationException{

        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(addFavoriteLocationRequest.getLocationId());

        checkDuplicateFavoriteLocation(addFavoriteLocationRequest.getLocationId(), email);

        FavoriteLocation favoriteLocation = FavoriteLocation.createFavoriteLocation(member, location, addFavoriteLocationRequest.getMemo());
        favoriteRepository.save(favoriteLocation);
        location.increaseFavoriteCount();

        return favoriteLocation.getId();
    }

    private void checkDuplicateFavoriteLocation(String locationId, String email) {

        FavoriteLocation favoriteLocation = favoriteRepository.duplicateFavoriteLocation(locationId, email);

        if (favoriteLocation != null) {
            throw new DuplicateFavoriteLocationException("이미 등록된 장소 입니다.");
        }
    }

    public Page<FavoriteLocationList> getFavoriteLocationList(Pageable pageable, String email) {

        return favoriteRepository.getFavoriteLocationListPage(pageable, email);
    }

    public void deleteFavoriteLocation(Integer favoriteLocationId){

        //중복 체크
        Long id = Long.valueOf(favoriteLocationId);
        FavoriteLocation favoriteLocation = favoriteRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        //decrease count
        Location location = favoriteLocation.getLocation();
        location.decreaseFavoriteCount();

        favoriteRepository.delete(favoriteLocation);
    }

    public Integer getMaxPage(Integer size) {

        Integer totalCount = Math.toIntExact(favoriteRepository.count());

        //page 는 0부터 시작하므로 +1 안해도 됨
        return (totalCount / size) ;
    }

    public Long updateMemo(UpdateMemoRequest updateMemoRequest) {

        Long id = Long.valueOf(updateMemoRequest.getFavoriteLocationId());
        FavoriteLocation favoriteLocation = favoriteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        favoriteLocation.updateMemo(updateMemoRequest.getMemo());

        return favoriteLocation.getId();
    }

}
