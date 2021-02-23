package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    // 우편 번호
    private String pl_id;

    // 장소 명
    private String pl_name;

    // 위도
    private double pl_lttd;

    // 경도
    private double pl_lngtd;

    // 출발지 횟수
    private int pl_start;

    // 도착지 횟수
    private int pl_finish;

}
