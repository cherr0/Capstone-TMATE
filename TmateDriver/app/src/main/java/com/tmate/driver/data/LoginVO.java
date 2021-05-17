package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    // 이메일 또는 휴대폰번호
    private String id;

    // 비밀번호
    private String password;

    // 권한 확인
    private String auth;

    // 회원코드
    private String m_id;

    // 회원 이름
    private String m_name;

    // Imei 값
    private String m_imei;
}
