package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {

    // 평점 코드
    private String r_code;

    // 사유
    private String r_reason;


    // 연관관계
    // 이용 코드
    private String merchant_uid;

    // 회원 코드
    private String m_id;

}
