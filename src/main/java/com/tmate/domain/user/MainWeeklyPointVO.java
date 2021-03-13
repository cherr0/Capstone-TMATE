package com.tmate.domain.user;

import lombok.Data;

import java.sql.Timestamp;

// 사용자 일주일간 포인트 내역
@Data
public class MainWeeklyPointVO {

    private String po_exact;

    private String po_result;

    private Timestamp po_time;

    private int m_point;
}
