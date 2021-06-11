package com.tmate.domain.driver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverHistoryVO {
    /* 사용처 :
        기사 운행이력 리스트 가져오기
     */

    // 동승 수
    private String together;

    // 기사 코드
    private String d_id;

    // 배차 코드
    private String dp_id;

    // 총 요금
    private int all_fare;

    // 출발지
    private String start_place;

    // 도착지
    private String finish_place;

    // 출발 시간
    private Timestamp start_time;

    // 도착 시간
    private Timestamp end_time;

    // 탑승자 명단
    private String name;

    // 평가 사유
    private String reason;

    // 좋아요 수
    private int like_cnt;

    // 싫어요 수
    private int hate_cnt;
}
