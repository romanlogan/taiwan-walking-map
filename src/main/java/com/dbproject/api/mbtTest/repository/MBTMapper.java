package com.dbproject.api.mbtTest.repository;

import com.dbproject.api.mbtTest.MBTCityDto;
import com.dbproject.api.mbtTest.MBTCityDtoCamel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MBTMapper extends MBTRepoInt {

    // 메소드명은 Mapper.xml 에 작성될 쿼리문의 ID
    MBTCityDtoCamel selectCity(@Param("postalAddressCity") String postalAddressCity);
    List<MBTCityDto> selectAll();
}
