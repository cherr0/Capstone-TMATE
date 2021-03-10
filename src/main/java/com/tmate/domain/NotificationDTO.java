package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDTO {

    /**
     *
     *
     * N_PHONE   NOT NULL VARCHAR2(11)
     * M_ID      NOT NULL VARCHAR2(14)
     * N_NAME             VARCHAR2(15)
     * N_WHETHER          CHAR(1)
     *
     */

    // 지인 전화 번호
    private String n_phone;

    // 회원 번호
    private String m_id;

    // 지인 이름
    private String n_name;

    // 활성화 : 0 비활성화 : 1
    private String n_whether;
}
