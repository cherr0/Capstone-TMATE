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

    // 기사 코드
    private String d_id;

    // 좋아요 횟수
    private int like_cnt;

    // 싫어요 횟수
    private int hate_cnt;

    // 동승 유무
    private String to_enabled;

    // 이용코드
    private String merchant_uid;

    // 총요금
    private int h_allFare;

    // 운행 시작 시간
    private Timestamp h_s_time;

    // 운행 종료 시간
    private Timestamp h_e_time;

    // 출발지
    private String h_s_place;

    // 도착지
    private String h_f_place;
}
