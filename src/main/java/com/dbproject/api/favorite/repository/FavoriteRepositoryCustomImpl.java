package com.dbproject.api.favorite.repository;

import com.dbproject.api.favorite.QFavoriteLocation;
import com.dbproject.api.favorite.dto.FavoriteLocationDto;
import com.dbproject.api.favorite.dto.FavoriteLocationList;
import com.dbproject.api.favorite.dto.QFavoriteLocationDto;
//import com.dbproject.dto.QFavoriteListResponse;
//import com.dbproject.entity.QFavoriteLocation;
//import com.dbproject.entity.QLocation;
//import com.dbproject.entity.QMember;
import com.dbproject.api.location.QLocation;
import com.dbproject.api.member.QMember;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.List;

public class FavoriteRepositoryCustomImpl implements FavoriteRepositoryCustom {


    private JPAQueryFactory queryFactory;

    public FavoriteRepositoryCustomImpl(EntityManager entityManager) {

        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public Page<FavoriteLocationDto>  getFavoriteLocationListPage(Pageable pageable, String email){

        QLocation location = QLocation.location;
        QFavoriteLocation favoriteLocation = QFavoriteLocation.favoriteLocation;
        QMember member = QMember.member;

        //select * from FavoriteLocation
//        join Location on FavoriteLocation.location_id = Location.location_id
//        join Member on FavoriteLocation.member_id = Member.member_id
        List<FavoriteLocationDto> content = queryFactory
                .select(
                        new QFavoriteLocationDto(
                                favoriteLocation.id,
                                favoriteLocation.memo,
                                location.locationId,
                                location.name,
                                location.address,
                                location.region,
                                location.locationPicture.picture1,
                                location.longitude,
                                location.latitude
                        )
                )
                .from(favoriteLocation)
                .join(favoriteLocation.location , location)
                .join(favoriteLocation.member , member)
                .where(member.email.eq(email))
                .orderBy(location.locationId.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(Wildcard.count)
                .from(favoriteLocation)
                .join(favoriteLocation.location , location)
                .join(favoriteLocation.member , member)
                .where(member.email.eq(email))
                .fetchOne();

        return new PageImpl<>(content, pageable, total);

    }
}
