package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private String m_id;  // 회원 코드
    private String m_name;  // 회원 이름
    private String m_regdate; // 회원 가입일자
    private String m_profile; // 회원 프로필
    private String m_imei;    // 회원 기기 일련번호
    private Date  m_birth;    // 회원 생년월일
    private String m_email;   // 회원 이메일
    private String m_house;   // 회원 집 주소
    private int m_n_use;      // 회원 일반 콜 이용
    private int m_to_use;     // 회원 동승 콜 이용
    private int m_count;        // 노쇼 카운트
    private String m_level;     // 회원 등급
    private int m_point;        // 회원 포인트 합계
    private String m_status;    // 회원 상태
    private String m_invite;    // 회원 초대 코드

}
