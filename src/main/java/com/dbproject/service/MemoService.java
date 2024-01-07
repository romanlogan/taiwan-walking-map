package com.dbproject.service;

import com.dbproject.dto.UpdateMemoRequest;
import com.dbproject.entity.FavoriteLocation;
import com.dbproject.repository.FavoriteRepository;
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
