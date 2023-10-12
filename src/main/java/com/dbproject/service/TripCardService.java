package com.dbproject.service;

import com.dbproject.entity.TripCard;
import com.dbproject.repository.TripCardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@Transactional
@RequiredArgsConstructor
public class TripCardService {

    private final TripCardRepository tripCardRepository;

    public void test() {

        TripCard tripCard = tripCardRepository.findById(1).orElseThrow(EntityNotFoundException::new);

        System.out.println(tripCard.toString());
    }
}
