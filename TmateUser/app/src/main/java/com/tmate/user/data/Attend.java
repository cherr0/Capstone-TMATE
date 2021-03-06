package com.tmate.user.data;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attend {
    // 1. 회원 코드
    private String m_id;

    // 2. 배차 코드
    private String dp_id;

    // 3. 사용포인트
    private int use_point;

    // 4. 참여 상태코드
    private String at_status;

    // 5. 좌석 번호
    private int seat;

    // 6. 노쇼 사유
    private String noshow;

    // 7. 금액
    private int amount;

    // 8. 기사 평가
    private String re_driver;

    // 9. 기사 평가 사유
    private String re_driver_reason;

    // 10. 1번 자리 평가
    private String re_one;

    // 11. 2번 자리 평가
    private String re_two;

    // 12. 3번 자리 평가
    private String re_three;

    // 조인시 연관되는 컬럼
    // 멤버 관련
    private String m_name;

    private Timestamp m_birth;

    private int m_n_use;

    private int m_t_use;
}
