package com.dbproject.api.mbtTest.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MBTMapper {

    // 메소드명은 Mapper.xml 에 작성될 쿼리문의 ID
    String selectCity(@Param("postalAddressCity") String postalAddressCity);
}
