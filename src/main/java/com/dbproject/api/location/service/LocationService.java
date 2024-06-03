package com.dbproject.api.location.service;

import com.dbproject.api.location.dto.LocationDtlResponse;
import com.dbproject.api.location.dto.RecommendLocationListRequest;
import com.dbproject.api.location.dto.RecommendLocationListResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LocationService {

     LocationDtlResponse getLocationDtl(String locationId);
     LocationDtlResponse getLocationDtlWithAuthUser(String locationId,String email);
     RecommendLocationListResponse getRecommendLocationList(RecommendLocationListRequest request, Pageable pageable);
     List<String> getTownListFrom(String region);

}
