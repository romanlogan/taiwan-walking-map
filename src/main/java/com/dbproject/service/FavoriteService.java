package com.dbproject.service;

import com.dbproject.dto.AddFavoriteLocationRequest;
import com.dbproject.dto.FavoriteListResponse;
import com.dbproject.entity.FavoriteLocation;
import com.dbproject.entity.Location;
import com.dbproject.entity.Member;
import com.dbproject.exception.DuplicateFavoriteLocationException;
import com.dbproject.repository.FavoriteRepository;
import com.dbproject.repository.LocationRepository;
import com.dbproject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        FavoriteLocation favoriteLocation = FavoriteLocation.createFavoriteLocation(member, location);
        favoriteRepository.save(favoriteLocation);

        return favoriteLocation.getId();
    }

    private void checkDuplicateFavoriteLocation(String locationId,String email) {

        FavoriteLocation favoriteLocation = favoriteRepository.duplicateFavoriteLocation(locationId, email);

        if (favoriteLocation != null) {
            throw new DuplicateFavoriteLocationException("이미 등록된 장소 입니다.");
        }
    }

    public Page<FavoriteListResponse> getFavoriteLocationList(Pageable pageable, Principal principal) {

        return favoriteRepository.getFavoriteLocationList(pageable, principal);
    }
}
