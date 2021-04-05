package com.tmate.security.handler;

import com.tmate.security.dto.AuthMemberDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ClubLoginSucessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public ClubLoginSucessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("-------------------------------");
        log.info("onAuthenticationSuccess ");

        AuthMemberDTO authMemberDTO = (AuthMemberDTO) authentication.getPrincipal();

        if(authMemberDTO.getM_id().substring(0,1).equals("m")) {

            redirectStrategy.sendRedirect(request, response, "/tuser/main");

        }else {
            redirectStrategy.sendRedirect(request, response, "/main");
        }
    }
}
