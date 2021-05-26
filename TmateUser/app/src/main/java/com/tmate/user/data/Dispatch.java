package com.tmate.user.data;

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

    // attend 참여
    // 사용 포인트
    private int use_point;

    // 참여 상태 코드
    private String at_status;

    // 좌석번호
    private int seat;

    // 노쇼 사유
    private String noshow;

    // 금액
    private int amount;

    // 기사 평가
    private String re_driver;

    // 기사 평가 사유
    private String re_driver_reason;

    // 1번 자리 평가
    private String re_one;

    // 2번 자리 평가
    private String re_two;

    // 3번 자리 평가
    private String re_three;

    // 결제 수단
    // 결제 고유 번호
    private String sid;

    // 결제수단
    private String pay_pg;

    // 카드 활성 유무
    private String active;

    // 카드사
    private String pay_company;

    // 사업자 등록 번호
    private String pay_com_reg;

    // 카드 타입
    private String card_type;

    // 카드 별칭
    private String pay_alias;

    /*
     *  값이 추가적으로 필요한 경우
     * */

    // 기사와 출발지와의 거리
    private double distance;
}
