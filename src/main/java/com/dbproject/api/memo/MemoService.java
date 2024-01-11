package com.dbproject.api.memo;

import com.dbproject.web.memo.UpdateMemoRequest;
import com.dbproject.api.favorite.FavoriteLocation;
import com.dbproject.api.favorite.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class MemoService {

    private final FavoriteRepository favoriteRepository;


    public Long updateMemo(UpdateMemoRequest updateMemoRequest) {

        Long id = Long.valueOf(updateMemoRequest.getFavoriteLocationId());
        FavoriteLocation favoriteLocation = favoriteRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        favoriteLocation.updateMemo(updateMemoRequest.getMemo());

        return favoriteLocation.getId();
    }




}
