package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinHistoryVO {

    // 이용코드 - 동승여부, 탑승일
    private String merchant_uid;

    // 회원 코드
    private String m_id;

    // 출발지
    private String h_s_place;
    private String h_s_lttd;
    private String h_s_lngtd;

    // 도착지
    private String h_f_place;
    private String h_f_lttd;
    private String h_f_lngtd;

    // 운행 시간
    private Timestamp h_s_time;
    private Timestamp h_e_time;

    // 차량 번호
    private String car_no;

    // 차량 모델
    private String car_model;

    // 기사명
    private String m_name;

}
