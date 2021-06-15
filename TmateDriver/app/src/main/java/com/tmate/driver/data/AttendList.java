package com.tmate.driver.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendList {

    // 회원 코드
    private String m_id;

    // 회원 이름
    private String m_name;

    // 나이
    private String age;

    // 성별
    private String gender;

    // 좌석번호
    private String seat;
}
