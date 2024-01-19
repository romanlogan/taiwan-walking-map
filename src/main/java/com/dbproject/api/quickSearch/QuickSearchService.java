package com.dbproject.api.quickSearch;


import com.dbproject.api.city.City;
import com.dbproject.api.city.CityRepository;
import com.dbproject.api.location.LocationRepository;
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
        QuickSearchCityDto quickSearchCityDto = QuickSearchCityDto.of(city);

        quickSearchResultDto.setCity(quickSearchCityDto);
    }


    public void findLocationPage(QuickSearchResultDto quickSearchResultDto,
                                                 FastSearchDto fastSearchDto,
                                                 Pageable pageable) {

        Page<QuickSearchLocationDto> locationPage = locationRepository.getLocationPageBySearch(fastSearchDto, pageable);
        quickSearchResultDto.setLocationPage(locationPage);
    }
}
