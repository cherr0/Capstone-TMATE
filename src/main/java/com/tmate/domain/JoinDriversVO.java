package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinDriversVO {

    // 기사 번호
    private String m_id;

    // 기사 이름
    private String m_name;

    // 기사 생년월일
    private Timestamp m_birth;

    // 기사 승인 날짜
    private Timestamp d_j_date;

    // 기사 차량 번호
    private String car_no;

    // 기사 차량량
    private String carmodel;
}
