package com.dbproject.api.location;

import com.dbproject.api.quickSearch.dto.QQuickSearchLocationDto;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
import com.dbproject.entity.QLocation;
import com.dbproject.api.quickSearch.dto.FastSearchDto;
import com.dbproject.api.quickSearch.dto.QuickSearchLocationDto;
import com.querydsl.core.types.OrderSpecifier;
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
                                location.longitude,
                                location.region

                        )
                )
                .from(location)
                .where(QLocation.location.region.eq(fastSearchDto.getSearchCity()),
                        searchNameByQuery(fastSearchDto.getSearchQuery()))
                .orderBy(QLocation.location.longitude.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QLocation.location)
                .where(QLocation.location.region.eq(fastSearchDto.getSearchCity()),
                        searchNameByQuery(fastSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<Location> getLocationPageByBtn(FastSearchDto fastSearchDto, Pageable pageable){

        List<Location> content = queryFactory
                .selectFrom(QLocation.location)
                .where(searchNameByQuery(fastSearchDto.getSearchQuery()))
                .orderBy(QLocation.location.locationId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(QLocation.location)
                .where(searchNameByQuery(fastSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }



    @Override
    public List<QuickSearchLocationDto> getLocationListByCond(QuickSearchFormRequest quickSearchFormRequest,
                                                              Pageable pageable){

        QLocation location = QLocation.location;

        List<QuickSearchLocationDto> quickSearchLocationDtoList = queryFactory
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
                                location.longitude,
                                location.region

                        )
                )
                .from(location)
                .where(QLocation.location.region.eq(quickSearchFormRequest.getSearchCity()),
                        QLocation.location.town.eq(quickSearchFormRequest.getSearchTown()),
                        searchNameByQuery(quickSearchFormRequest.getSearchQuery()),
                        searchOpenTimeCond(quickSearchFormRequest.getOpenTimeCond()),
                        searchFeeCond(quickSearchFormRequest.getFeeCond()),
                        searchPicCond(quickSearchFormRequest.getPicCond())
                        )
                .orderBy(orderByOrderType(quickSearchFormRequest.getOrderType()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return quickSearchLocationDtoList;
    }


    private OrderSpecifier<String> orderByOrderType(String orderType) {

        if (orderType.equals("name")) {

            return QLocation.location.name.asc();
        }else if (orderType.equals("id")) {

            return QLocation.location.locationId.asc();
        }

        return null;
    }

    private BooleanExpression searchPicCond(String picCond) {

        if (picCond.equals("true")) {

            return QLocation.location.locationPicture.picture1.isNotNull();
        }else{
            return null;
        }
    }

    private BooleanExpression searchFeeCond(String feeCond) {

        if (feeCond.equals("true")) {

            return QLocation.location.ticketInfo.isNotNull();
        }else{
            return null;
        }
    }

    private BooleanExpression searchOpenTimeCond(String openTimeCond) {

        if (openTimeCond.equals("true")) {

            return QLocation.location.openTime.isNotNull();
        }else{
            return null;
        }


    }


    private BooleanExpression searchNameByQuery(String searchQuery) {

        return QLocation.location.name.like("%" + searchQuery + "%");
    }

}
