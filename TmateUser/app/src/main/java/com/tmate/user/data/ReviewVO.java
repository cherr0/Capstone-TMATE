package com.tmate.user.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewVO {

    // 회원 코드
    private String m_id;

    // 배차 코드
    private String dp_id;

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

}
