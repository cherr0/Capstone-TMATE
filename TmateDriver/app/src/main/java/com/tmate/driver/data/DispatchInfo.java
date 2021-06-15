package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DispatchInfo {

    // 배차 코드
    private String dp_id;

    // 출발지
    private String start_place;

    // 출발지 위도
    private double start_lat;

    // 출발지 경도
    private double start_lng;

    // 도착지
    private String finish_place;

    // 도착지 위도
    private double finish_lat;

    // 도착지 경도
    private double finish_lng;

    // 방 생성자 회원 코드
    private String m_id;

    /* -------------------
        방 생성자 선호운행옵션
       ------------------- */
    private int do_hurry; // 급정거 옵션
    private int do_navi; // 네비게이션대로 이동
    private int do_quiet; // 조용히 가고 싶음
    private int do_animal; // 반려동물 동반
    private int do_load; // 짐 유무
    private int do_child; // 유아 동반
}
