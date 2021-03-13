package com.tmate.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReceiptDTO {

    /*
    *
    * IMP_UID      NOT NULL VARCHAR2(40)
        RE_TIME      NOT NULL TIMESTAMP(6)
        RE_CRN                CHAR(3)
        RE_AMT                NUMBER(7)
        USE_CASH              CHAR(1)
        CUSTOMER_UID NOT NULL VARCHAR2(21)
        M_ID         NOT NULL VARCHAR2(14)
    *
    * */

    // 거래 고유 번호
    private String imp_uid;
    // 결제 시간
    private Timestamp re_rime;
    // 화폐 단위
    private String re_crn;
    // 금액
    private int re_amt;
    // 현금 결제 유무
    private String use_cash;
    // 빌링키
    private String customer_uid;
    // 회원 코드
    private String m_id;
}
