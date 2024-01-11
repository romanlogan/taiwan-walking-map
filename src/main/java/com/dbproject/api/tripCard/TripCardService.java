package com.dbproject.api.tripCard;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TripCardService {

    private final TripCardRepository tripCardRepository;

    public List<TripCard> getTripCardAddress(String city) {

        List<TripCard> tripCardList = tripCardRepository.findTripCardByCity(city);

//        List<String> tripCardAddressList = new ArrayList<>();
//
//
//        for (TripCard tripcard : tripCardList
//        ) {
//            tripCardAddressList.add(tripcard.getAddress());
//        }
//
//        System.out.println(tripCardList.size());

        return tripCardList;
    }
}
