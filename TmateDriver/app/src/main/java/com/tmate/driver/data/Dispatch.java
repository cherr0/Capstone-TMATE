package com.tmate.driver.data;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dispatch {
    // 1. 배차 코드 (PK) : TIMESTAMP + 최대인원(1)
    private String dp_id;

    // 2. 매칭 상태
    private String dp_status;

    // 3. 총 요금
    private int all_fare;

    // 4. 출발지
    private String start_place;

    // 5. 도착지
    private String finish_place;

    // 6. 출발지 위도
    private double start_lat;

    // 7. 출발지 경도
    private double start_lng;

    // 8. 도착지 위도
    private double finish_lat;

    // 9. 도착지 경도
    private double finish_lng;

    // 10. 운행 시작 시간
    private Timestamp start_time;

    // 11. 운행 종료 시간
    private Timestamp end_time;

    // 12. 모이는 시간
    private Timestamp meet_time;

    // 13. 예상 소요시간
    private int ep_time;

    // 14. 이동 거리
    private int ep_distance;

    // 15. 현재 인원
    private int cur_people;

    // 16. 기사 코드 (FK)
    private String d_id;

    // 17. 생성자코드 (FK)
    private String m_id;

    /*
     *  JOIN시 생길수 있는 Column
     * */

    // 멤버 관련
    private String m_name;

    private double m_lat;

    private double m_lng;

    // 차 관련
    private String car_no;

    private String car_model;



    /*
     *  값이 추가적으로 필요한 경우
     * */

    // 기사와 출발지와의 거리
    private double distance;

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
