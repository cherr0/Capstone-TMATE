package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {

    // 이벤트 코드 (3) + 시퀀스
    private String e_id;

    // 이벤트 이름
    private String e_kind;

    // 이벤트 내용
    private String e_contents;

    // 이벤트 시작 날짜
    private Timestamp e_s_date;

    // 이벤트 종료 날짜
    private Timestamp e_e_date;
}
