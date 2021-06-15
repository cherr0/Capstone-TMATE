package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfileVO {

    // 회원코드
    private String m_id;

    // 이름
    private String m_name;

    // 휴대폰번호
    private String phone;

    // 생일
    private String m_birth;

    // 가입일자
    private String m_regdate;

    // 이용 횟수
    private int use_count;

    // 노쇼 횟수
    private int noshow_count;

    // 보유 포인트
    private int unused_point;

    // 프로필 사진
    private String m_profile;
}
