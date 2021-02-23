package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


// 사용자 상세 페이지 포인트 관리
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinPointVO {

    private String po_exact;

    private int po_result;

    private String po_course;

    private Timestamp po_time;

    private int m_point;
}
