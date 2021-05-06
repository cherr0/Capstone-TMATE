package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TogetherDTO {

    private String m_id;

    private String merchant_uid;



    /*
    * 매칭 방 인원은 최대 3인 까지 가능 (2~3)
    *
    * 좌석 번호의 경우
    * EX) 운전석   1번
    *     2번     3번
    * */

    // 2, 3
    private int to_max;

    private int to_people;

    // 1, 2, 3
    private int to_seat;
}
