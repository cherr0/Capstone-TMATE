package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinPointVO {

    private String po_exact;

    private int po_result;

    private String po_course;

    private String po_time;

    private int m_point;
}
