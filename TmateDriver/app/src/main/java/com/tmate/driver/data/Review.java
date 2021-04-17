package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Review {

    // 평점 코드
    private String r_code;

    // 사유
    private String r_reason;

    // 평점코드 (함수 사용)
    private String reaction;

    // 연관관계
    // 이용 코드
    private String merchant_uid;

    // 회원 코드
    private String m_id;

}
