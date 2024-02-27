package com.dbproject.config;

import com.dbproject.api.mbtTest.repository.MBTRepoInt;
import com.dbproject.api.mbtTest.repository.MBTRepository;
import com.dbproject.api.mbtTest.service.MBTService;
import com.dbproject.api.mbtTest.service.MBTServiceImpl;
import com.dbproject.api.mbtTest.service.MBTServiceJPAImpl;
import com.dbproject.web.mbtTest.MBTController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


//DI 컨테이너
@Configuration
public class AppConfig {

    @Bean
    public MBTService mbtService() {
//        return new MBTServiceImpl(new MBTRepoInt());
        return new MBTServiceJPAImpl();
    }

//
//    @Bean
//    public MBTRepoInt mbtRepoInt() {
//     return MBTRepository;
//    }


}
