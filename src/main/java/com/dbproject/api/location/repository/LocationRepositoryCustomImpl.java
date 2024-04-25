package com.dbproject.api.location.repository;

import com.dbproject.api.location.Location;
import com.dbproject.api.location.QLocation;
import com.dbproject.api.location.QRecLocationListResponse;
import com.dbproject.api.location.dto.LocationDto;
import com.dbproject.api.location.dto.QLocationDto;
import com.dbproject.api.location.dto.RecLocationListRequest;
import com.dbproject.api.location.dto.RecommendLocationDto;
import com.dbproject.api.quickSearch.dto.QQuickSearchLocationDto;
import com.dbproject.api.quickSearch.dto.QuickSearchFormRequest;
//import com.dbproject.entity.QLocation;
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
    public Page<RecommendLocationDto> getLocationPageByCity(RecLocationListRequest request, Pageable pageable){

        QLocation location = QLocation.location;


        List<RecommendLocationDto> content = queryFactory
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
                .where(searchByRegion(request),
                        searchNameByQuery(request.getSearchQuery()),
                        searchByTown(request))
                .orderBy(QLocation.location.locationId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(QLocation.location)
                .where(searchByRegion(request),
                        searchNameByQuery(request.getSearchQuery()),
                        searchByTown(request))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }

    private static BooleanExpression searchByRegion(RecLocationListRequest request) {
        return QLocation.location.region.eq(request.getSearchArrival());
    }

    private static BooleanExpression searchByTown(RecLocationListRequest request) {
        return QLocation.location.town.like("%"+ request.getSearchTown()+"%");

//        return QLocation.location.name.like("%" + searchQuery + "%");
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

    @Override
    public List<LocationDto> findTop10RecommendLocationList(String region){

        QLocation location = QLocation.location;


        return queryFactory
                .select(
                        new QLocationDto(
                                location.locationId,
                                location.name,
                                location.commentCount,
                                location.tolDescribe,
                                location.description,
                                location.tel,
                                location.address,
                                location.region,
                                location.town,
                                location.travellingInfo,
                                location.openTime,
                                location.longitude,
                                location.latitude,
                                location.website,
                                location.parkingInfo,
                                location.ticketInfo,
                                location.remarks,
                                location.locationPicture.picture1
                        )
                )
                .from(location)
                .where(QLocation.location.region.eq(region))
                .orderBy(QLocation.location.favoriteCount.desc())        //1등부터 10등까지
                .limit(10)
                .fetch();
    }
//

    private OrderSpecifier orderByOrderType(String orderType) {

        if (orderType.equals("name")) {

            return QLocation.location.name.asc();

        }else if (orderType.equals("id")) {

            return QLocation.location.locationId.asc();

        }else if (orderType.equals("comment")) {

            return com.dbproject.api.location.QLocation.location.commentCount.desc();
        }else if (orderType.equals("favorite")) {

//            2개 이상의 조건으로 order by 하려면 ?
            return com.dbproject.api.location.QLocation.location.favoriteCount.desc();
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
