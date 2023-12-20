package com.dbproject.service;


import com.dbproject.dto.FastSearchDto;
import com.dbproject.dto.QuickSearchResultDto;
import com.dbproject.entity.City;
import com.dbproject.entity.Location;
import com.dbproject.repository.CityRepository;
import com.dbproject.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class QuickSearchService {

    private final LocationRepository locationRepository;

    private final CityRepository cityRepository;

    public QuickSearchResultDto getQuickSearchPage(FastSearchDto fastSearchDto, Pageable pageable) {

        QuickSearchResultDto quickSearchResultDto = new QuickSearchResultDto();

        //추천 장소 페이징 저장
        findLocationPage(quickSearchResultDto, fastSearchDto ,pageable);

        //도시 저장
        findCityDtl(quickSearchResultDto, fastSearchDto.getSearchCity());

        return quickSearchResultDto;

    }

    private void findCityDtl(QuickSearchResultDto quickSearchResultDto, String searchCity) {

        City city = cityRepository.findBypostalAddressCity(searchCity);
        quickSearchResultDto.setCity(city);

    }


    public void findLocationPage(QuickSearchResultDto quickSearchResultDto,
                                                 FastSearchDto fastSearchDto,
                                                 Pageable pageable) {

        Page<Location> locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);
        quickSearchResultDto.setLocationPage(locationPage);
    }
}
