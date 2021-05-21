package com.tmate.domain;

import com.google.inject.internal.util.Sets;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    // 좋아요 횟수
    private int like;

    // 싫어요 횟수
    private int dislike;

    // 소셜 여부
    private String m_s_flag;

    // 소셜 이메일
    private String s_email;

    // 패스워드 -임시
    private String password;

    // 멤버 위치
    private double m_lat;

    private double m_lng;

    // 권한
    private List<MemberRole> role = new ArrayList<>();





}
