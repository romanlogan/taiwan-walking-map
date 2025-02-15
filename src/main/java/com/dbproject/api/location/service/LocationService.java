package com.dbproject.api.location.service;

import com.dbproject.api.location.dto.LocationDtlResponse;
import com.dbproject.api.location.dto.RecommendLocationListRequest;
import com.dbproject.api.location.dto.RecommendLocationListResponse;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface LocationService {

     LocationDtlResponse getLocationDtl(String locationId, String name);
     RecommendLocationListResponse getRecommendLocationList(RecommendLocationListRequest request, Pageable pageable);
     List<String> getTownListFrom(String region);

}
