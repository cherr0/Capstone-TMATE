package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SidebarProfile {
    /* 사용처 :
        기사 사이드바 프로필
     */

    // 기사 상태 0 : 휴식, 1: 대기, 2: 운행중
    private int m_status;

    // 기사 이름
    private String m_name;

    // 차량 번호
    private String car_no;

    // 차종
    private String car_model;

    // 차량 분류
    private String car_kind;

    // 차량 색상
    private String car_color;

    // 구분코드 c: 법인, i: 개인
    private String corp_code;
}
