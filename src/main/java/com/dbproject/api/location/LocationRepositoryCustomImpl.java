package com.dbproject.api.location;

import com.dbproject.api.location.LocationRepositoryCustom;
import com.dbproject.dto.*;
import com.dbproject.api.location.Location;
import com.dbproject.entity.QLocation;
import com.dbproject.web.quickSearch.FastSearchDto;
import com.dbproject.web.quickSearch.QuickSearchLocationDto;
import com.dbproject.web.location.RecLocationListRequest;
import com.dbproject.web.location.RecLocationListResponse;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public LocationRepositoryCustomImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<RecLocationListResponse> getLocationPageByCity(RecLocationListRequest request, Pageable pageable){

        QLocation location = QLocation.location;


        List<RecLocationListResponse> content = queryFactory
                .select(
                        new QRecLocationListResponse(
                                location.locationId,
                                location.locationPicture.picture1,
                                location.name,
                                location.address,
                                location.openTime,
                                location.ticketInfo,
                                location.website,
                                location.tel,
                                location.latitude,
                                location.longitude
                        )
                )
                .from(location)
                .where(QLocation.location.region.eq(request.getSearchArrival()))
                .orderBy(QLocation.location.locationId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(QLocation.location)
                .where(QLocation.location.region.eq(request.getSearchArrival()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression searchByLike(String searchQuery) {

        return QLocation.location.name.like("%" + searchQuery + "%");

    }

    @Override
    public Page<QuickSearchLocationDto> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable){

        QLocation location = QLocation.location;

        List<QuickSearchLocationDto> content = queryFactory
                .select(
                        new QQuickSearchLocationDto(
                                location.locationId,
                                location.locationPicture.picture1,
                                location.name,
                                location.address,
                                location.openTime,
                                location.ticketInfo,
                                location.website,
                                location.tel,
                                location.latitude,
                                location.longitude
                        )
                )
                .from(location)
                .where(QLocation.location.region.eq(fastSearchDto.getSearchCity()),
                        searchByLike(fastSearchDto.getSearchQuery()))
                .orderBy(QLocation.location.longitude.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QLocation.location)
                .where(QLocation.location.region.eq(fastSearchDto.getSearchCity()),
                        searchByLike(fastSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable){

        List<Location> content = queryFactory
                .selectFrom(QLocation.location)
                .where(searchByLike(fastSearchDto.getSearchQuery()))
                .orderBy(QLocation.location.locationId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(QLocation.location)
                .where(searchByLike(fastSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
}
