package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PointDTO {

    // 포인트 시퀀스
    private int po_id;

    // 회원 코드
    private String m_id;

    // 포인트 경로
    private String po_course;

    // 포인트 구분 코드
    private String po_exact;

    // 포인트
    private String po_result;

    // 변동 시간
    private String po_time;

    // 이벤트 코드
    private String e_id;

    // 잔여 요금 - DB에는 없음
    private int po_point;

}
