package com.dbproject.api.favorite;

import com.dbproject.web.favorite.AddFavoriteLocationRequest;
import com.dbproject.web.favorite.FavoriteListResponse;
import com.dbproject.api.location.Location;
import com.dbproject.api.member.Member;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.api.location.LocationRepository;
import com.dbproject.api.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;

@Service
@Transactional
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final LocationRepository locationRepository;


    public Long addFavoriteList(AddFavoriteLocationRequest addFavoriteLocationRequest, String email) throws DuplicateFavoriteLocationException{



        Member member = memberRepository.findByEmail(email);
        Location location = locationRepository.findByLocationId(addFavoriteLocationRequest.getLocationId());

        checkDuplicateFavoriteLocation(addFavoriteLocationRequest.getLocationId(), email);

        FavoriteLocation favoriteLocation = FavoriteLocation.createFavoriteLocation(member, location, addFavoriteLocationRequest.getMemo());
        favoriteRepository.save(favoriteLocation);

        return favoriteLocation.getId();
    }

    private void checkDuplicateFavoriteLocation(String locationId,String email) {

        FavoriteLocation favoriteLocation = favoriteRepository.duplicateFavoriteLocation(locationId, email);

        if (favoriteLocation != null) {
            throw new DuplicateFavoriteLocationException("이미 등록된 장소 입니다.");
        }
    }

    public Page<FavoriteListResponse> getFavoriteLocationList(Pageable pageable, String email) {

        return favoriteRepository.getFavoriteLocationList(pageable, email);
    }

    public void deleteFavoriteLocation(String favoriteLocationId){

        //중복 체크

        Long id = Long.valueOf(favoriteLocationId);
        FavoriteLocation favoriteLocation = favoriteRepository.findById(id).orElseThrow(EntityNotFoundException::new);

        favoriteRepository.delete(favoriteLocation);
    }
}
