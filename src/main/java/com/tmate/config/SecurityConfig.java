package com.tmate.config;

import com.tmate.security.handler.ClubLoginSucessHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Log4j2
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {


        http.authorizeRequests()
                .antMatchers("/").permitAll();

        http.csrf().disable();
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/");
        http.oauth2Login().successHandler(sucessHandler());
    }

    @Bean
    public ClubLoginSucessHandler sucessHandler() {
        return new ClubLoginSucessHandler(passwordEncoder());
    }
}
