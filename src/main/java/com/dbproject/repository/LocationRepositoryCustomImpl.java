package com.dbproject.repository;

import com.dbproject.entity.Location;
import com.dbproject.entity.QLocation;
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
    public Page<Location> getLocationPageByCity(String arriveCity, Pageable pageable){

        List<Location> content = queryFactory
                .selectFrom(QLocation.location)
                .where(QLocation.location.PostalAddressCity.eq(arriveCity))
                .orderBy(QLocation.location.attractionId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory.select(Wildcard.count).from(QLocation.location)
                .where(QLocation.location.PostalAddressCity.eq(arriveCity))
                .fetchOne()
                ;

        return new PageImpl<>(content, pageable, total);
    }
}
