package com.tmate.driver.data;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CallHistory {

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

    // 조인 해서 가져온다.
    private String m_name;

    // 방 만든 사람 기준
    private String h_made_flag;

    // 기사 코드
    private String d_id;

    // DB 컬럼에는 없다. 현재 인원수
    private int to_people;

    // 최대 인원
    private int to_max;

    private String h_ep_fare;

    private String h_ep_time;

    private String h_ep_distance;


    // 출발지와 떨어진 거리
    private double distance1;

    // 도착지와 떨어진 거리
    private double distance2;
}
