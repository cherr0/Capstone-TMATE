package com.tmate.security.service;

import com.tmate.domain.MemberDTO;
import com.tmate.mapper.Membermapper;
import com.tmate.security.dto.AuthMemberDTO;
import com.tmate.service.MemberService;
import edu.emory.mathcs.backport.java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class OAuth2UserDetailsService extends DefaultOAuth2UserService {

    private final Membermapper membermapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        log.info("------------------------------");
        log.info("userRequest:"+userRequest);

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: " + clientName);
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("==============================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v);
        });

        String email = null;

        if (clientName.equals("Google")) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL: " + email);
        MemberDTO memberDTO = findMember(email);
        log.info("이 유저의 권한은? : " + memberDTO.getRole());

        log.info("이유저 소셜 계정은?",memberDTO.getS_email());
        log.info("oAuth2User.getAttribute"+oAuth2User.getAttributes());
        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                memberDTO.getS_email(),
                "1111",
                true,
                memberDTO.getRole().stream().map(i ->
                        new SimpleGrantedAuthority("ROLE_"+i.getM_role())).collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        authMemberDTO.setM_name(memberDTO.getM_name());
        authMemberDTO.setM_id(memberDTO.getM_id());
        authMemberDTO.setM_email(memberDTO.getM_email());
        authMemberDTO.setM_profile(memberDTO.getM_profile());
        authMemberDTO.setM_level(memberDTO.getM_level());
        authMemberDTO.setM_birth(memberDTO.getM_birth());
        authMemberDTO.setM_house(memberDTO.getM_house());

        log.info("인증된 계정 : " + authMemberDTO);

        return authMemberDTO;
    }


    private MemberDTO findMember(String email) {


        // App 이용하는 계정인 경우
        MemberDTO member = membermapper.findSocialMember(email);
        String m_id = member.getM_id();
        member.setRole(membermapper.findRoleList(m_id));

        return member;


    }

}
