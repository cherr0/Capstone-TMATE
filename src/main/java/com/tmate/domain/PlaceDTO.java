package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    // 주소 검색 코드 (카카오)
    private String pl_id;

    // 주소
    private String pl_address;

    // 장소 명
    private String pl_name;

    // 위도
    private double pl_lat;

    // 경도
    private double pl_lng;

    // 출발지 횟수
    private int pl_start;

    // 도착지 횟수
    private int pl_finish;

}
