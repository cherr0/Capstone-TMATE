package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinHistoryVO {

    // 이용코드 - 동승여부, 탑승일
    private String merchant_uid;


    // 출발지
    private String h_s_place;


    // 도착지
    private String h_f_place;


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
