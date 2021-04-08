package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoryDTO {

    // 동승 여부, 탑승일
    private String merchant_uid;

    // 출발지
    private String h_s_place;
    private double h_s_lttd;
    private double h_s_lngtd;

    // 도착지
    private String h_f_place;
    private double h_f_lttd;
    private double h_f_lngtd;

    // 운행시간
    private Timestamp h_s_time;
    private Timestamp h_e_time;

    // 탑승 순위
    private int cnt;


    // 총 요금
    private int h_allfare;

    // 요청 사항
    private String h_request;

    // 연관관계
    // 회원코드
    private String m_id;

    // 기사 코드
    private String d_id;


}
