package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {

    // 기사 코드
    private String d_id;

    // 기사 자격증 번호
    private String d_license_no;

    // 기사 승인 날짜
    private Timestamp d_j_date;

    // 은행사
    private String bank_company;

    // 계좌 번호
    private String d_acnum;

    // 계좌 등록일
    private String d_regdate;

    // 기사명
    private String m_name;

    // 생년 월일
    private Timestamp m_birth;

    // 차량
    private String car_model;

    // 법인 구분 코드
    private String corp_code;


}
