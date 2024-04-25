package com.dbproject.api.location.service;

import com.dbproject.api.location.dto.LocationDtlResponse;
import com.dbproject.api.location.dto.RecLocationListRequest;
import com.dbproject.api.location.dto.RecommendLocationListResponse;
import org.springframework.data.domain.Pageable;

public interface LocationService {

     LocationDtlResponse getLocationDtl(String locationId);
     LocationDtlResponse getLocationDtlWithAuthUser(String locationId,String email);
     RecommendLocationListResponse getRecommendLocationListResponse(RecLocationListRequest request, Pageable pageable);

}
