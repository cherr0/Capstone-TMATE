package com.tmate.domain;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class JoinHistoryVO {

    // 이용코드 - 동승여부, 탑승일
    private String merchant_uid;


    // 출발지, 출발지 위도, 출발지 경도
    private String h_s_place;
    private double h_s_lttd;
    private double h_s_lngtd;



    // 총요금
    private String h_allfare;


    // 도착지, 도착지 위도 , 경도
    private String h_f_place;
    private double h_f_lttd;
    private double h_f_lngtd;

    // 매칭 상태
    private String h_status;

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
