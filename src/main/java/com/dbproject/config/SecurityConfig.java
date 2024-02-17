package com.dbproject.config;

import com.dbproject.api.member.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;

//    @Bean
//    public UserDetailsService userDetailsService() {
//
//        InMemoryUserDetailsManager userDetailService = new InMemoryUserDetailsManager();
//
//        UserDetails user = User.withUsername("john")
//                .password("1234")
//                .authorities("read")
//                .build();
//
//        userDetailService.createUser(user);
//
//        return userDetailService;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS);

        http.formLogin()
                .loginPage("/members/login")
                .defaultSuccessUrl("/")
                .usernameParameter("email")
                .failureUrl("/members/login/error")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/");

        http.httpBasic();

        http.authorizeRequests()
                .mvcMatchers("/","/members/**","/img/**","/css/**").permitAll()
                .mvcMatchers("/myPage/**","/favorite/**","/comment/**","/memo/**","/hangOut/**").authenticated()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll();
//                .anyRequest().authenticated();      //나머지 경로는 모두 인증을 요구
    }




    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());
    }





}
