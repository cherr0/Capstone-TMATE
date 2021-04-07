package com.tmate.security.dto;

import com.tmate.domain.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Log4j2
@Getter
@Setter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {

    // 권한, 성별, 기본키
    private String m_id;

    // 이름
    private String m_name;

    // 생년월일
    private Timestamp m_birth;

    // 이용 횟수
    private int m_n_use;

    // 동승 횟수
    private int m_t_use;

    // 보유 포인트
    private int m_point;

    // 가입 일자
    private Timestamp m_regdate;

    // 비밀번호
    private String password;

    // 이메일
    private String m_email;

    // 노쇼 카운트
    private int m_count;

    // imei
    private String m_imei;

    // 등급
    private String m_level;

    // 상태
    private String m_status;

    // 프로필 사진
    private String m_profile;

    // 집 주소
    private String m_house;

    private int like;

    private int dislike;

    // 소셜 이메일
    private String s_email;

    // 소셜 여부
    private String m_s_flag;

    // 소셜 여부
    private boolean fromSocial;

    private Map<String, Object> attr;

    private List<MemberRole> memberRoles;


    public AuthMemberDTO(String username,
                         String password,
                         boolean fromSocial,
                         Collection<? extends GrantedAuthority> authorities,
                         Map<String, Object> attr) {
        this(username, password, fromSocial ,authorities);
        this.attr = attr;
    }


    public AuthMemberDTO(String username,
                         String password,
                         boolean fromSocial,
                         Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.s_email = username;
        this.password = password;
        this.fromSocial = fromSocial;

    }



    @Override

    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    @Override
    public String getName() {
        return this.m_name;
    }
}
