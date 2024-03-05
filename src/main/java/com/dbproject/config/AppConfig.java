package com.dbproject.config;

import com.dbproject.api.mbtTest.repository.MBTRepoInt;
import com.dbproject.api.mbtTest.repository.MBTRepository;
import com.dbproject.api.mbtTest.service.MBTService;
import com.dbproject.api.mbtTest.service.MBTServiceImpl;
import com.dbproject.api.mbtTest.service.MBTServiceJPAImpl;
import com.dbproject.web.mbtTest.MBTController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//DI 컨테이너
@Configuration
public class AppConfig {

//    여기다 Bean 주입 안하면 컨트롤러에서 @Qualifier 를 써야함 -> 책임 분리 X
//    @Bean
//    public MBTController mbtController() {
//        return new MBTController(MBTService mbtService);
//    }
//
//    public MBTService


//    service 에서 jpa 와 mybatis 를 나눴으므로 repository 는 공통으로 안묶어도 되고 바로 자동주입 받으면 됨
//    @Bean
//    public MBTService mbtService() {
////        return new MBTServiceImpl(new MBTRepoInt());
//        return new MBTServiceJPAImpl();
//    }

//
//    @Bean
//    public MBTRepoInt mbtRepoInt() {
//     return MBTRepository;
//    }


}
