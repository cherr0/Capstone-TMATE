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
    *   TID          NOT NULL VARCHAR2(40)
    *   AID                   VARCHAR2(30)
        RE_TIME      NOT NULL TIMESTAMP(6)
        RE_CRN                CHAR(3)
        RE_AMT                NUMBER(7)
        USE_CASH              CHAR(1)
        SID          NOT NULL VARCHAR2(21)
        M_ID         NOT NULL VARCHAR2(14)
        D_ID         NOT NULL VARCHAR2(14)
    *
    * */

    // 거래 고유 번호
    private String tid;
    // 이력 번호
    private String aid;
    // 결제 시간
    private String re_rime;
    // 화폐 단위
    private String re_crn;
    // 금액
    private int re_amt;
    // 현금 결제 유무
    private String use_cash;
    // 결제 고유 번호
    private String sid;
    // 회원 코드
    private String m_id;
    // 기사 코드
    private String d_id;
}
