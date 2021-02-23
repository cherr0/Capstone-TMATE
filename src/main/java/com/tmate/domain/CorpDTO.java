package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorpDTO {

    // 구분 코드
    private String corp_code;

    // 차량 번호
    private String car_no;

    // 기사 코드
    private String d_id;

    // 회사 명
    private String corp_company;
}
