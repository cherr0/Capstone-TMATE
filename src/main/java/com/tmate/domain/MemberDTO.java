package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {

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



}
