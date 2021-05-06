package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverProfile {
    /* 사용처 :
        기사 프로필 정보 가져오기
     */

    // 회원 코드
    private String m_id;

    // 회원 이름
    private String m_name;

    // 이메일
    private String m_email;

    // 생년월일
    private Timestamp m_birth;

    // 은행사
    private String bank_company;

    // 계좌번호
    private String d_acnum;

    // 기사 등록 일자
    private Timestamp d_j_date;

    // 일반 운행 수
    private int no_cnt;

    // 동승 운행 수
    private int to_cnt;

    // 기사 총 수입 (함수 사용)
    private int all_fare;

    // 기사 이 달 수입 (함수 사용)
    private int month_fare;

    // 휴대폰번호 (함수 사용)
    private String phone;

    // 성별 (함수 사용)
    private String gender;
}
