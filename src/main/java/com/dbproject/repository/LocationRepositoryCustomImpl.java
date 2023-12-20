package com.dbproject.repository;

import com.dbproject.dto.FastSearchDto;
import com.dbproject.entity.Location;
import com.dbproject.entity.QLocation;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class LocationRepositoryCustomImpl implements LocationRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public LocationRepositoryCustomImpl(EntityManager em) {

        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Location> getLocationPageByCity(String arriveCity, Pageable pageable){

        List<Location> content = queryFactory
                .selectFrom(QLocation.location)
                .where(QLocation.location.region.eq(arriveCity))
                .orderBy(QLocation.location.locationId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QLocation.location)
                .where(QLocation.location.region.eq(arriveCity))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression searchByLike(String searchQuery) {

        return QLocation.location.name.like("%" + searchQuery + "%");

    }

    @Override
    public Page<Location> getLocationPageBySearch(FastSearchDto fastSearchDto, Pageable pageable){

        List<Location> content = queryFactory
                .selectFrom(QLocation.location)
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

        long total = queryFactory.select(Wildcard.count).from(QLocation.location)
                .where(searchByLike(fastSearchDto.getSearchQuery()))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
}
